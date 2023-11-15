package com.example.filmus.viewmodel.main

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.domain.UIState
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.createApiService
import com.example.filmus.domain.database.profile.ProfileDatabase
import com.example.filmus.domain.database.reviews.UserReviewDatabase
import com.example.filmus.domain.main.MainUseCase
import com.example.filmus.domain.main.Movie
import com.example.filmus.domain.profile.CacheProfileUseCase
import com.example.filmus.domain.userReviews.UserReviewsUseCase
import com.example.filmus.repository.TokenManager
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
    }

    fun getMovies(force: Boolean = false, refresh: Boolean = false) {
        if (force) {
            movies.clear()
            currentPage = 1
            screenState.value = UIState.LOADING
        } else if (refresh) {
            screenState.value = UIState.REFRESHING
        } else {
            screenState.value = UIState.LOADING
        }
        viewModelScope.launch {
            try {
                getProfileReviews()
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
        viewModelScope.launch {
            getMovies(refresh = true)
        }
    }

    private suspend fun getProfileReviews() {
        val userReviewDatabase = UserReviewDatabase.getDatabase(
            context = tokenManager.context
        )
        val userReviewDao = userReviewDatabase.userReviewDao()
        val userReviewsUseCase = UserReviewsUseCase(UserReviewsRepository(userReviewDao))
        userReviewsUseCase.getProfileReviews(getProfileId()).let { reviews ->
            if (userReviews != reviews)
                userReviews.clear()
            userReviews.addAll(reviews)
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