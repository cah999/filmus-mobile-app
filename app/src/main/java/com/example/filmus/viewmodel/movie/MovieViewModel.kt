package com.example.filmus.viewmodel.movie

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.DetailedMovieResponse
import com.example.filmus.api.ReviewRequest
import com.example.filmus.api.createApiService
import com.example.filmus.domain.UIState
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
    var screenState = mutableStateOf(UIState.LOADING)
    var initialItemPosition = mutableIntStateOf(0)

    init {
        screenState.value = UIState.LOADING
        getMovieDetails()
        getUserReviews()
        getIsFavorite()
        screenState.value = UIState.DEFAULT
    }

    private fun getMovieDetails() {
        viewModelScope.launch {
            val apiService = createApiService()
            val movieRepository = MovieRepository(apiService, userManager = userManager)
            val movieUseCase = MovieUseCase(movieRepository)
            val result = movieUseCase.getMovieDetails(movieId)
            if (result != null) {
                getUserReviews()
                movieDetails.value = result
            }
        }
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
            userManager.getProfileReviews().let { reviewID ->
                userReviews.addAll(reviewID)
            }
            existsReviewID.value =
                movieDetails.value?.reviews?.find { it?.id in userReviews }?.id
        }
    }

    private fun removeUserReview() {
        viewModelScope.launch {
            userManager.removeProfileReview(reviewID.value)
        }
        userReviews.remove(reviewID.value)
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

    fun addReview(initialPosition: Int) {
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            try {
                val apiService = createApiService(userManager.getToken())
                val movieRepository = MovieRepository(apiService, userManager = userManager)
                val movieUseCase = MovieUseCase(movieRepository)
                movieUseCase.addReview(
                    movieId, ReviewRequest(
                        reviewText = review.value,
                        rating = rating.intValue,
                        isAnonymous = isAnonymous.value
                    )
                )
                getMovieDetails()
            } finally {
                initialItemPosition.intValue = initialPosition
                screenState.value = UIState.DEFAULT

            }
        }
    }

    fun editReview(initialPosition: Int) {
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            try {
                val apiService = createApiService(userManager.getToken())
                val movieRepository = MovieRepository(apiService, userManager = userManager)
                val movieUseCase = MovieUseCase(movieRepository)
                movieUseCase.editReview(
                    movieId, reviewID.value, ReviewRequest(
                        reviewText = review.value,
                        rating = rating.intValue,
                        isAnonymous = isAnonymous.value
                    )
                )
                getMovieDetails()
            } finally {
                initialItemPosition.intValue = initialPosition
                screenState.value = UIState.DEFAULT

            }
        }
    }

    fun removeReview(reviewID: String, initialPosition: Int) {
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            try {
                val apiService = createApiService(userManager.getToken())
                val movieRepository = MovieRepository(apiService, userManager = userManager)
                val movieUseCase = MovieUseCase(movieRepository)
                movieUseCase.removeReview(movieId, reviewID)
                removeUserReview()
                getMovieDetails()
            } finally {
                initialItemPosition.intValue = initialPosition
                screenState.value = UIState.DEFAULT

            }
        }
    }
}