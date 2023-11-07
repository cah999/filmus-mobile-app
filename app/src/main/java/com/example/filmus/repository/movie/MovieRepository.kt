package com.example.filmus.repository.movie

import android.util.Log
import com.example.filmus.api.ApiService
import com.example.filmus.api.DetailedMovieResponse

class MovieRepository(
    private val apiService: ApiService
) {
    suspend fun getDetailedMovie(id: String): DetailedMovieResponse? {
        val response = apiService.getDetailedMovie(id)
        Log.d("MovieRepository", "getDetailedMovie: $response")

        if (response.isSuccessful) {
            val detailedMovie = response.body()
            if (detailedMovie != null) {
                return detailedMovie
            } else {
                throw Exception("Empty response body")
            }
        } else {
            throw Exception("Error: ${response.code()}")
        }
    }
}
