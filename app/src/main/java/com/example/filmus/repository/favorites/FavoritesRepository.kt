package com.example.filmus.repository.favorites

import com.example.filmus.api.ApiService
import com.example.filmus.domain.main.Movie

class FavoritesRepository(
    private val apiService: ApiService
) {
    suspend fun getFavorites(): List<Movie> {
        val response = apiService.getFavorites()
        if (response.isSuccessful) {
            return response.body()?.results ?: emptyList()
        }
        return emptyList()
    }

    suspend fun addFavorite(movieID: String) {
        val response = apiService.addFavorite(movieID)
        if (response.isSuccessful) {
            return
        }
        throw Exception("Error adding favorite")
    }


    suspend fun removeFavorite(movieID: String) {
        val response = apiService.removeFavorite(movieID)
        if (response.isSuccessful) {
            return
        }
        throw Exception("Error removing favorite")
    }
}