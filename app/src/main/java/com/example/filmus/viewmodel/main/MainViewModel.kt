package com.example.filmus.viewmodel.main

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.domain.TokenManager
import com.example.filmus.domain.UIState
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.createApiService
import com.example.filmus.domain.database.profile.ProfileDatabase
import com.example.filmus.domain.database.reviews.UserReviewDatabase
import com.example.filmus.domain.main.MainUseCase
import com.example.filmus.domain.main.Movie
import com.example.filmus.domain.profile.CacheProfileUseCase
import com.example.filmus.domain.userReviews.UserReviewsUseCase
import com.example.filmus.repository.main.MainRepository
import com.example.filmus.repository.profile.CacheProfileRepository
import com.example.filmus.repository.userReviews.UserReviewsRepository
import kotlinx.coroutines.launch

class MainViewModel(private val tokenManager: TokenManager) : ViewModel() {
    val screenState = mutableStateOf(UIState.DEFAULT)
    val movies = mutableStateListOf<Movie>()
    private var currentPage = 1
    val userReviews = mutableListOf<String>()

    init {
        getMovies()
        getProfileReviews()
    }

    fun getMovies(force: Boolean = false) {
        if (force) {
            screenState.value = UIState.REFRESHING
            movies.clear()
            currentPage = 1
        }
        viewModelScope.launch {
            try {
                val apiService = createApiService()
                val mainRepository = MainRepository(apiService)
                val mainUseCase = MainUseCase(mainRepository)
                when (val result = mainUseCase.getMovies(currentPage)) {
                    is ApiResult.Success -> {
                        if (result.data != null) {
                            movies.addAll(result.data)
                        }
                    }

                    is ApiResult.Unauthorized -> {
                        screenState.value = UIState.UNAUTHORIZED
                    }

                    is ApiResult.Error -> {
                        screenState.value = UIState.ERROR
                    }
                }
            } finally {
                screenState.value = UIState.DEFAULT
            }
        }
    }

    fun loadNextPage() {
        currentPage++
        if (currentPage > 10) {
            currentPage = 10
        }
        screenState.value = UIState.REFRESHING
        viewModelScope.launch {
            getMovies()
        }
    }

    private fun getProfileReviews() {
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            try {
                userReviews.clear()
                val userReviewDatabase = UserReviewDatabase.getDatabase(
                    context = tokenManager.context
                )
                val userReviewDao = userReviewDatabase.userReviewDao()
                val userReviewsUseCase = UserReviewsUseCase(UserReviewsRepository(userReviewDao))
                userReviewsUseCase.getProfileReviews(getProfileId()).let { reviews ->
                    userReviews.addAll(reviews)
                }
            } finally {
                screenState.value = UIState.DEFAULT
            }
        }
    }

    private suspend fun getProfileId(): String {
        val profileDatabase = ProfileDatabase.getDatabase(
            context = tokenManager.context
        )
        val profileDao = profileDatabase.profileDao()
        val profileUseCase = CacheProfileUseCase(CacheProfileRepository(profileDao))
        return profileUseCase.getProfileId()
    }
}