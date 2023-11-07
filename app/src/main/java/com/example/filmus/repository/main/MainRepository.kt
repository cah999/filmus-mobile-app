package com.example.filmus.repository.main

import android.util.Log
import com.example.filmus.api.ApiService
import com.example.filmus.domain.main.Movie

class MainRepository(
    private val apiService: ApiService
) {
    suspend fun getMovies(page: Int): List<Movie> {
        val response = apiService.getMovies(page)
        Log.d("MainRepository", "getMovies response: $response, ${response.isSuccessful}")
        if (response.isSuccessful) {
            return response.body()?.results ?: emptyList()
        }
        return emptyList()
    }
}