package com.example.filmus.viewmodel.movie

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.DetailedMovieResponse
import com.example.filmus.api.ReviewRequest
import com.example.filmus.api.createApiService
import com.example.filmus.domain.UserManager
import com.example.filmus.domain.favorite.FavoritesUseCase
import com.example.filmus.domain.main.Movie
import com.example.filmus.domain.movie.MovieUseCase
import com.example.filmus.repository.favorites.FavoritesRepository
import com.example.filmus.repository.movie.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(movieId: String, private val userManager: UserManager) : ViewModel() {
    private val movieId = movieId
    var movieDetails = mutableStateOf(null as DetailedMovieResponse?)
    var isFavorite = mutableStateOf(false)
    var rating = mutableIntStateOf(0)
    var review = mutableStateOf("")
    var reviewID = mutableStateOf("")
    var isAnonymous = mutableStateOf(false)
    var userReviews = mutableListOf("")
    var existsReviewID = mutableStateOf(null as String?)

    init {
        getIsFavorite()
        getUserReviews()
    }

    suspend fun getMovieDetails() {
        val apiService = createApiService()
        val movieRepository = MovieRepository(apiService, userManager = userManager)
        val movieUseCase = MovieUseCase(movieRepository)
        val result = movieUseCase.getMovieDetails(movieId)
        if (result != null) movieDetails.value = result
    }


    private fun getIsFavorite() {
        viewModelScope.launch {
            val cachedFavorites = userManager.getFavorites()
            if (cachedFavorites.contains(movieId)) {
                isFavorite.value = true
            } else {
                val apiService = createApiService(userManager.getToken())
                val favoritesRepository = FavoritesRepository(apiService)
                val favoritesUseCase = FavoritesUseCase(favoritesRepository)
                val result: List<Movie> = favoritesUseCase.getFavorites()
                userManager.addFavorites(result.map { it.id }, true)
                if (result.any { it.id == movieId }) {
                    isFavorite.value = true
                }
            }
        }
    }

    private fun getUserReviews() {
        viewModelScope.launch {
            userManager.getProfileReviews().let {
                userReviews.addAll(it)
            }
        }
    }

    fun addFavorite(movieID: String) {
        viewModelScope.launch {
            val apiService = createApiService(userManager.getToken())
            val favoritesRepository = FavoritesRepository(apiService)
            val favoritesUseCase = FavoritesUseCase(favoritesRepository)
            userManager.addFavorites(listOf(movieID))
            favoritesUseCase.addFavorite(movieID)
        }
    }

    fun removeFavorite(movieID: String) {
        viewModelScope.launch {
            val apiService = createApiService(userManager.getToken())
            val favoritesRepository = FavoritesRepository(apiService)
            val favoritesUseCase = FavoritesUseCase(favoritesRepository)
            userManager.removeFavorites(listOf(movieID))
            favoritesUseCase.removeFavorite(movieID)


        }
    }

    fun addReview() {
        viewModelScope.launch {
            val apiService = createApiService(userManager.getToken())
            val movieRepository = MovieRepository(apiService, userManager = userManager)
            val movieUseCase = MovieUseCase(movieRepository)
            movieUseCase.addReview(
                movieId, ReviewRequest(
                    reviewText = review.value,
                    rating = rating.value,
                    isAnonymous = isAnonymous.value
                )
            )
        }
    }

    fun editReview() {
        viewModelScope.launch {
            val apiService = createApiService(userManager.getToken())
            val movieRepository = MovieRepository(apiService, userManager = userManager)
            val movieUseCase = MovieUseCase(movieRepository)
            movieUseCase.editReview(
                movieId, reviewID.value, ReviewRequest(
                    reviewText = review.value,
                    rating = rating.value,
                    isAnonymous = isAnonymous.value
                )
            )
        }
    }

    fun removeReview(reviewID: String) {
        viewModelScope.launch {
            val apiService = createApiService(userManager.getToken())
            val movieRepository = MovieRepository(apiService, userManager = userManager)
            val movieUseCase = MovieUseCase(movieRepository)
            movieUseCase.removeReview(movieId, reviewID)
        }
    }


}