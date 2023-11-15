package com.example.filmus.repository.movie

import com.example.filmus.common.Constants
import com.example.filmus.domain.MoshiProvider
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.ApiService
import com.example.filmus.domain.movie.DetailedMovieResponse
import com.example.filmus.domain.movie.ReviewRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class MovieRepository(
    private val apiService: ApiService
) {
    suspend fun getDetailedMovie(id: String): ApiResult<DetailedMovieResponse?> {
        return try {
            val response = apiService.getDetailedMovie(id)
            if (response.isSuccessful) {
                val detailedMovie = response.body()
                if (detailedMovie != null) {
                    ApiResult.Success(detailedMovie)
                } else {
                    ApiResult.Error(Constants.UNKNOWN_ERROR)
                }
            } else if (response.code() == 401) {
                ApiResult.Unauthorized()
            } else {
                ApiResult.Error(Constants.UNKNOWN_ERROR)
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }

    suspend fun addReview(movieID: String, request: ReviewRequest): ApiResult<Nothing> {
        return try {
            val reviewRequestBody = MoshiProvider.reviewRequestAdapter.toJson(request)
                .toRequestBody("application/json".toMediaTypeOrNull())
            val response = apiService.addReview(movieID, reviewRequestBody)
            if (response.isSuccessful) {
                ApiResult.Success()
            } else if (response.code() == 401) {
                ApiResult.Unauthorized()
            } else {
                ApiResult.Error(Constants.UNKNOWN_ERROR)
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }

    suspend fun editReview(
        movieID: String, reviewID: String, request: ReviewRequest
    ): ApiResult<Nothing> {
        return try {
            val reviewRequestBody = MoshiProvider.reviewRequestAdapter.toJson(request)
                .toRequestBody("application/json".toMediaTypeOrNull())
            val response = apiService.editReview(movieID, reviewID, reviewRequestBody)
            when {
                response.isSuccessful -> {
                    ApiResult.Success()
                }

                response.code() == 400 -> {
                    ApiResult.Error(Constants.BAD_REQUEST_ERROR, 400)
                }

                response.code() == 401 -> {
                    ApiResult.Unauthorized()
                }

                else -> {
                    ApiResult.Error(Constants.UNKNOWN_ERROR)
                }
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }

    suspend fun removeReview(movieID: String, reviewID: String): ApiResult<Nothing> {
        return try {
            val response = apiService.removeReview(movieID, reviewID)
            if (response.isSuccessful) {
                ApiResult.Success()
            } else if (response.code() == 401) {
                ApiResult.Unauthorized()
            } else {
                ApiResult.Error(Constants.UNKNOWN_ERROR)
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }
}

