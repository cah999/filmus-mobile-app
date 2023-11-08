package com.example.filmus.repository.movie

import android.util.Log
import com.example.filmus.api.ApiService
import com.example.filmus.api.DetailedMovieResponse
import com.example.filmus.api.ReviewRequest
import com.example.filmus.domain.MoshiProvider
import com.example.filmus.domain.UserManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class MovieRepository(
    private val apiService: ApiService, private val userManager: UserManager
) {
    suspend fun getDetailedMovie(id: String): DetailedMovieResponse? {
        val response = apiService.getDetailedMovie(id)
        if (response.isSuccessful) {
            val detailedMovie = response.body()
            if (detailedMovie != null) {
                val userID = userManager.getProfileID()
                val review = detailedMovie.reviews.find {
                    (it?.author?.userId ?: "") == userID
                }
                Log.d("MovieRepository", "getDetailedMovie: $review")
                if (review != null) {
                    Log.d("MovieRepository", "getDetailedMovie: ${review.id}")
                    userManager.addUserReview(userID, review.id)
                }

                return detailedMovie
            } else {
                throw Exception("Empty response body")
            }
        } else {
            throw Exception("Error: ${response.code()}")
        }
    }


    suspend fun addReview(movieID: String, request: ReviewRequest) {
        val reviewRequestBody = MoshiProvider.reviewRequestAdapter.toJson(request)
            .toRequestBody("application/json".toMediaTypeOrNull())
        val response = apiService.addReview(movieID, reviewRequestBody)
        if (response.isSuccessful) {
            return
        }
        throw Exception("Error adding review")
    }

    suspend fun editReview(
        movieID: String, reviewID: String, request: ReviewRequest
    ) {
        val reviewRequestBody = MoshiProvider.reviewRequestAdapter.toJson(request)
            .toRequestBody("application/json".toMediaTypeOrNull())
        val response = apiService.editReview(movieID, reviewID, reviewRequestBody)
        if (response.isSuccessful) {
            return
        }
        throw Exception("Error editing review")

    }

    suspend fun removeReview(movieID: String, reviewID: String) {
        val response = apiService.removeReview(movieID, reviewID)
        if (response.isSuccessful) {
            return
        }
        throw Exception("Error removing review")
    }
}
