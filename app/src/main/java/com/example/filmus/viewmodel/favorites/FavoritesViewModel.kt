package com.example.filmus.viewmodel.favorites

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.createApiService
import com.example.filmus.domain.UIState
import com.example.filmus.domain.UserManager
import com.example.filmus.domain.favorite.FavoritesUseCase
import com.example.filmus.domain.main.Movie
import com.example.filmus.repository.favorites.FavoritesRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(userManager: UserManager) : ViewModel() {
    val screenState = mutableStateOf(UIState.DEFAULT)
    var movies = mutableStateListOf<Movie>()
    val userManager = userManager
    val userReviews = mutableStateOf(listOf<String>())


    fun getFavorites() {
        viewModelScope.launch {
            try {
                screenState.value = UIState.LOADING
                val apiService = createApiService(userManager.getToken())
                val favoritesRepository = FavoritesRepository(apiService)
                val favoritesUseCase = FavoritesUseCase(favoritesRepository)
                val favorites = favoritesUseCase.getFavorites()
                userManager.addFavorites(favorites.map { it.id }, true)
                movies.clear()
                movies.addAll(favorites)
            } finally {
                screenState.value = UIState.DEFAULT
            }
        }
    }

    fun addFavorite(movieID: String) {
        viewModelScope.launch {
            try {
                screenState.value = UIState.LOADING
                val apiService = createApiService(userManager.getToken())
                val favoritesRepository = FavoritesRepository(apiService)
                val favoritesUseCase = FavoritesUseCase(favoritesRepository)
                favoritesUseCase.addFavorite(movieID)
                userManager.addFavorites(listOf(movieID))
                getFavorites()
            } finally {
                screenState.value = UIState.DEFAULT
            }
        }
    }

    fun removeFavorite(movieID: String) {
        viewModelScope.launch {
            try {
                screenState.value = UIState.LOADING
                val apiService = createApiService(userManager.getToken())
                val favoritesRepository = FavoritesRepository(apiService)
                val favoritesUseCase = FavoritesUseCase(favoritesRepository)
                favoritesUseCase.removeFavorite(movieID)
                userManager.removeFavorites(listOf(movieID))
                getFavorites()
            } finally {
                screenState.value = UIState.DEFAULT
            }
        }
    }

    fun getProfileReviews() {
        viewModelScope.launch {
            userManager.getProfileReviews().let { reviews ->
                Log.d("FavoritesViewModel", "getProfileReviews: $reviews")
                userReviews.value = reviews
            }
        }
    }
}