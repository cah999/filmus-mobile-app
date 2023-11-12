package com.example.filmus.viewmodel.movie

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.common.Constants
import com.example.filmus.domain.TokenManager
import com.example.filmus.domain.UIState
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.createApiService
import com.example.filmus.domain.database.favorites.FavoritesDatabase
import com.example.filmus.domain.database.profile.ProfileDatabase
import com.example.filmus.domain.database.reviews.UserReviewDatabase
import com.example.filmus.domain.favorite.FavoritesCacheUseCase
import com.example.filmus.domain.favorite.FavoritesUseCase
import com.example.filmus.domain.movie.DetailedMovieResponse
import com.example.filmus.domain.movie.MovieUseCase
import com.example.filmus.domain.movie.ReviewRequest
import com.example.filmus.domain.profile.CacheProfileUseCase
import com.example.filmus.domain.userReviews.UserReviewsUseCase
import com.example.filmus.repository.favorites.FavoritesCacheRepository
import com.example.filmus.repository.favorites.FavoritesRepository
import com.example.filmus.repository.movie.MovieRepository
import com.example.filmus.repository.profile.CacheProfileRepository
import com.example.filmus.repository.userReviews.UserReviewsRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val movieId: String, private val tokenManager: TokenManager) :
    ViewModel() {
    var movieDetails = mutableStateOf(null as DetailedMovieResponse?)
    var isFavorite = mutableStateOf(false)
    var rating = mutableIntStateOf(0)
    var review = mutableStateOf("")
    var reviewID = mutableStateOf("")
    var isAnonymous = mutableStateOf(false)
    var userReviews = mutableListOf("")
    var existsReviewID = mutableStateOf(null as String?)
    var screenState = mutableStateOf(UIState.LOADING)
    var newReview = mutableStateOf(false)
    var newFavorite = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

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
            val movieRepository = MovieRepository(apiService)
            val movieUseCase = MovieUseCase(movieRepository)
            when (val result = movieUseCase.getMovieDetails(movieId)) {
                is ApiResult.Success -> {
                    if (result.data != null) {
                        val userID = getProfileId()
                        getUserReviews()
                        movieDetails.value = result.data
                        val review = result.data.reviews.find {
                            (it?.author?.userId ?: "") == userID
                        }
                        if (review != null) {
                            val userReviewDatabase = UserReviewDatabase.getDatabase(
                                context = tokenManager.context
                            )
                            val userReviewDao = userReviewDatabase.userReviewDao()
                            val userReviewsUseCase =
                                UserReviewsUseCase(UserReviewsRepository(userReviewDao))
                            userReviewsUseCase.addReview(userID, review.id)
                        }
                    }
                }

                is ApiResult.Unauthorized -> {
                    screenState.value = UIState.UNAUTHORIZED
                }

                is ApiResult.Error -> {
                    screenState.value = UIState.ERROR
                }
            }
        }
    }


    private fun getIsFavorite() {
        viewModelScope.launch {
            val favoritesDatabase = FavoritesDatabase.getDatabase(
                context = tokenManager.context
            )
            val favoritesDao = favoritesDatabase.favoritesDao()
            val cacheFavoritesUseCase =
                FavoritesCacheUseCase(FavoritesCacheRepository(favoritesDao))
            val cachedFavorites = cacheFavoritesUseCase.getFavorites(getProfileId())
            if (cachedFavorites.contains(movieId)) {
                isFavorite.value = true
            } else {
                val apiService = createApiService(tokenManager.getToken())
                val favoritesRepository = FavoritesRepository(apiService)
                val favoritesUseCase = FavoritesUseCase(favoritesRepository)
                when (val result = favoritesUseCase.getFavorites()) {
                    is ApiResult.Success -> {
                        if (result.data != null) {
                            cacheFavoritesUseCase.addFavorites(
                                result.data.map { it.id },
                                getProfileId(),
                                true
                            )
                            if (result.data.any { it.id == movieId }) {
                                isFavorite.value = true
                            }

                        }
                    }

                    is ApiResult.Unauthorized -> {
                        screenState.value = UIState.UNAUTHORIZED
                    }

                    is ApiResult.Error -> {
                        screenState.value = UIState.ERROR
                    }
                }

            }
        }
    }

    private fun getUserReviews() {
        viewModelScope.launch {
            val userReviewDatabase = UserReviewDatabase.getDatabase(
                context = tokenManager.context
            )
            val userReviewDao = userReviewDatabase.userReviewDao()
            val userReviewsUseCase = UserReviewsUseCase(UserReviewsRepository(userReviewDao))

            userReviewsUseCase.getProfileReviews(getProfileId()).let { reviewID ->
                userReviews.addAll(reviewID)
            }
            existsReviewID.value =
                movieDetails.value?.reviews?.find { it?.id in userReviews }?.id
        }
    }

    private fun removeUserReview() {
        viewModelScope.launch {
            val userReviewDatabase = UserReviewDatabase.getDatabase(
                context = tokenManager.context
            )
            val userReviewDao = userReviewDatabase.userReviewDao()
            val userReviewsUseCase = UserReviewsUseCase(UserReviewsRepository(userReviewDao))
            userReviewsUseCase.removeReview(reviewID.value)
        }
        userReviews.remove(reviewID.value)
    }

    fun addFavorite(movieID: String) {
        viewModelScope.launch {
            val apiService = createApiService(tokenManager.getToken())
            val favoritesRepository = FavoritesRepository(apiService)
            val favoritesUseCase = FavoritesUseCase(favoritesRepository)
            val favoritesDatabase = FavoritesDatabase.getDatabase(
                context = tokenManager.context
            )
            val favoritesDao = favoritesDatabase.favoritesDao()
            val cacheFavoritesUseCase =
                FavoritesCacheUseCase(FavoritesCacheRepository(favoritesDao))
            cacheFavoritesUseCase.addFavorites(listOf(movieID), getProfileId())
            when (favoritesUseCase.addFavorite(movieID)) {
                is ApiResult.Success -> {
                    newFavorite.value = true
                }

                is ApiResult.Unauthorized -> {
                    screenState.value = UIState.UNAUTHORIZED
                }

                is ApiResult.Error -> {
                    screenState.value = UIState.ERROR
                    errorMessage.value = Constants.UNKNOWN_ERROR
                }
            }
        }
    }

    fun removeFavorite(movieID: String) {
        viewModelScope.launch {
            val apiService = createApiService(tokenManager.getToken())
            val favoritesRepository = FavoritesRepository(apiService)
            val favoritesUseCase = FavoritesUseCase(favoritesRepository)
            val favoritesDatabase = FavoritesDatabase.getDatabase(
                context = tokenManager.context
            )
            val favoritesDao = favoritesDatabase.favoritesDao()
            val cacheFavoritesUseCase =
                FavoritesCacheUseCase(FavoritesCacheRepository(favoritesDao))
            cacheFavoritesUseCase.removeFavorites(listOf(movieID), getProfileId())
            when (favoritesUseCase.removeFavorite(movieID)) {
                is ApiResult.Success -> {
                    newFavorite.value = true
                }

                is ApiResult.Unauthorized -> {
                    screenState.value = UIState.UNAUTHORIZED
                }

                is ApiResult.Error -> {
                    screenState.value = UIState.ERROR
                    errorMessage.value = Constants.UNKNOWN_ERROR
                }
            }
        }
    }

    fun addReview() {
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            try {
                val apiService = createApiService(tokenManager.getToken())
                val movieRepository = MovieRepository(apiService)
                val movieUseCase = MovieUseCase(movieRepository)
                when (
                    movieUseCase.addReview(
                        movieId, ReviewRequest(
                            reviewText = review.value,
                            rating = rating.intValue,
                            isAnonymous = isAnonymous.value
                        )
                    )) {
                    is ApiResult.Success -> {
                        getMovieDetails()
                    }

                    is ApiResult.Unauthorized -> {
                        screenState.value = UIState.UNAUTHORIZED
                    }

                    is ApiResult.Error -> {
                        screenState.value = UIState.ERROR
                        errorMessage.value = Constants.UNKNOWN_ERROR
                    }
                }
            } finally {
                newReview.value = true
                screenState.value = UIState.DEFAULT

            }
        }
    }

    fun editReview() {
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            try {
                val apiService = createApiService(tokenManager.getToken())
                val movieRepository = MovieRepository(apiService)
                val movieUseCase = MovieUseCase(movieRepository)
                when (
                    val res = movieUseCase.editReview(
                        movieId, reviewID.value, ReviewRequest(
                            reviewText = review.value,
                            rating = rating.intValue,
                            isAnonymous = isAnonymous.value
                        )
                    )) {
                    is ApiResult.Success -> {
                        getMovieDetails()
                        screenState.value = UIState.DEFAULT
                    }

                    is ApiResult.Unauthorized -> {
                        screenState.value = UIState.UNAUTHORIZED
                    }

                    is ApiResult.Error -> {
                        if (res.code == 400) {
                            errorMessage.value = Constants.REVIEW_EDIT_ERROR
                        } else {
                            errorMessage.value = Constants.UNKNOWN_ERROR
                        }
                        screenState.value = UIState.ERROR
                    }
                }
            } finally {
                newReview.value = true
            }
        }
    }

    fun removeReview(reviewID: String) {
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            try {
                val apiService = createApiService(tokenManager.getToken())
                val movieRepository = MovieRepository(apiService)
                val movieUseCase = MovieUseCase(movieRepository)
                movieUseCase.removeReview(movieId, reviewID)
                removeUserReview()
                getMovieDetails()
            } finally {
                newReview.value = true
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