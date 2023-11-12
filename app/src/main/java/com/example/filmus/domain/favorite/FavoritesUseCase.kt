package com.example.filmus.domain.favorite

import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.main.Movie
import com.example.filmus.repository.favorites.FavoritesRepository

class FavoritesUseCase(private val favoritesRepository: FavoritesRepository) {
    suspend fun getFavorites(): ApiResult<List<Movie>> {
        return favoritesRepository.getFavorites()
    }

    suspend fun addFavorite(movieID: String): ApiResult<Nothing> {
        return favoritesRepository.addFavorite(movieID)
    }

    suspend fun removeFavorite(movieID: String): ApiResult<Nothing> {
        return favoritesRepository.removeFavorite(movieID)
    }


}