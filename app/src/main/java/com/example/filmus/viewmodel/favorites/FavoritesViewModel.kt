package com.example.filmus.viewmodel.favorites

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.domain.TokenManager
import com.example.filmus.domain.UIState
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.createApiService
import com.example.filmus.domain.database.favorites.FavoritesDatabase
import com.example.filmus.domain.database.profile.ProfileDatabase
import com.example.filmus.domain.database.reviews.UserReviewDatabase
import com.example.filmus.domain.favorite.FavoritesCacheUseCase
import com.example.filmus.domain.favorite.FavoritesUseCase
import com.example.filmus.domain.main.Movie
import com.example.filmus.domain.profile.CacheProfileUseCase
import com.example.filmus.domain.userReviews.UserReviewsUseCase
import com.example.filmus.repository.favorites.FavoritesCacheRepository
import com.example.filmus.repository.favorites.FavoritesRepository
import com.example.filmus.repository.profile.CacheProfileRepository
import com.example.filmus.repository.userReviews.UserReviewsRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val tokenManager: TokenManager) : ViewModel() {
    val screenState = mutableStateOf(UIState.DEFAULT)
    var movies = mutableStateListOf<Movie>()
    val userReviews = mutableStateOf(listOf<String>())

    init {
        getFavorites()
        getProfileReviews()
    }

    fun getFavorites() {
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            try {
                val apiService = createApiService(tokenManager.getToken())
                val favoritesRepository = FavoritesRepository(apiService)
                val favoritesUseCase = FavoritesUseCase(favoritesRepository)
                when (val result = favoritesUseCase.getFavorites()) {
                    is ApiResult.Success -> {
                        if (result.data != null) {
                            addCacheFavorites(
                                result.data.map { it.id },
                            )
                            movies.clear()
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

    private fun addCacheFavorites(favorites: List<String>) {

        viewModelScope.launch {
            val favoritesDatabase = FavoritesDatabase.getDatabase(
                context = tokenManager.context
            )
            val favoritesDao = favoritesDatabase.favoritesDao()
            val cacheFavoritesUseCase =
                FavoritesCacheUseCase(FavoritesCacheRepository(favoritesDao))
            cacheFavoritesUseCase.addFavorites(
                favorites, getProfileId(), true
            )
        }
    }


    fun removeFavorite(movieID: String) {
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            try {
                val apiService = createApiService(tokenManager.getToken())
                val favoritesRepository = FavoritesRepository(apiService)
                val favoritesUseCase = FavoritesUseCase(favoritesRepository)
                when (favoritesUseCase.removeFavorite(movieID)) {
                    is ApiResult.Success -> {
                        removeCacheFavorites(movieID)
                        getFavorites()
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

    private fun removeCacheFavorites(movieID: String) {
        viewModelScope.launch {
            val favoritesDatabase = FavoritesDatabase.getDatabase(
                context = tokenManager.context
            )
            val favoritesDao = favoritesDatabase.favoritesDao()
            val cacheFavoritesUseCase =
                FavoritesCacheUseCase(FavoritesCacheRepository(favoritesDao))
            cacheFavoritesUseCase.removeFavorites(
                listOf(movieID), getProfileId()
            )
        }
    }

    fun getProfileReviews() {
        viewModelScope.launch {
            val userReviewDatabase = UserReviewDatabase.getDatabase(
                context = tokenManager.context
            )
            val userReviewDao = userReviewDatabase.userReviewDao()
            val userReviewsUseCase = UserReviewsUseCase(UserReviewsRepository(userReviewDao))

            userReviewsUseCase.getProfileReviews(getProfileId()).let { reviews ->
                userReviews.value = reviews
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