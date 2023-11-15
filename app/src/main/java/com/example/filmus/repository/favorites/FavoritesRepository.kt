package com.example.filmus.repository.favorites

import com.example.filmus.common.Constants
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.ApiService
import com.example.filmus.domain.main.Movie


class FavoritesRepository(
    private val apiService: ApiService
) : IFavoritesRepository {
    override suspend fun getFavorites(): ApiResult<List<Movie>> {
        try {
            val response = apiService.getFavorites()
            return if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                ApiResult.Success(movies)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (errorBody.isNullOrEmpty()) {
                    Constants.UNKNOWN_ERROR
                } else {
                    errorBody
                }
                if (response.code() == 401) {
                    ApiResult.Unauthorized(message)
                } else {
                    ApiResult.Error(message)
                }
            }
        } catch (e: Exception) {
            return ApiResult.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }

    override suspend fun addFavorite(movieID: String): ApiResult<Nothing> {
        try {
            val response = apiService.addFavorite(movieID)
            return if (response.isSuccessful) {
                ApiResult.Success()
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (errorBody.isNullOrEmpty()) {
                    Constants.UNKNOWN_ERROR
                } else {
                    errorBody
                }
                if (response.code() == 401) {
                    ApiResult.Unauthorized(message)
                } else {
                    ApiResult.Error(message)
                }
            }
        } catch (e: Exception) {
            return ApiResult.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }

    override suspend fun removeFavorite(movieID: String): ApiResult<Nothing> {
        try {
            val response = apiService.removeFavorite(movieID)
            return if (response.isSuccessful) {
                ApiResult.Success()
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (errorBody.isNullOrEmpty()) {
                    Constants.UNKNOWN_ERROR
                } else {
                    errorBody
                }
                if (response.code() == 401) {
                    ApiResult.Unauthorized(message)
                } else {
                    ApiResult.Error(message)
                }
            }
        } catch (e: Exception) {
            return ApiResult.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }
}
