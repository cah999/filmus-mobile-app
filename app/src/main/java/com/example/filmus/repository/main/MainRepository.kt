package com.example.filmus.repository.main

import com.example.filmus.common.Constants
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.ApiService
import com.example.filmus.domain.main.Movie

class MainRepository(
    private val apiService: ApiService
) {
    suspend fun getMovies(page: Int): ApiResult<List<Movie>> {
        return try {
            val response = apiService.getMovies(page)
            if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                ApiResult.Success(movies)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (errorBody.isNullOrEmpty()) {
                    Constants.UNKNOWN_ERROR
                } else {
                    errorBody
                }
                ApiResult.Error(message)
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }
}
