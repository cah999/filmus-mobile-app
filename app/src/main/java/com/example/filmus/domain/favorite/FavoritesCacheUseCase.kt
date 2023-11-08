package com.example.filmus.domain.favorite

import com.example.filmus.repository.favorites.FavoritesCacheRepository

class FavoritesCacheUseCase(
    private val favoritesCacheRepository: FavoritesCacheRepository
) {
    suspend fun getFavorites(userID: String): List<String> {
        return favoritesCacheRepository.getFavorites(userID)
    }

    suspend fun addFavorites(favorites: List<String>, userID: String, clearOld: Boolean = false) {
        if (clearOld) favoritesCacheRepository.clearFavorites(userID)
        favoritesCacheRepository.addFavorites(favorites, userID)
    }

    suspend fun removeFavorites(favorites: List<String>, userID: String) {
        favoritesCacheRepository.removeFavorites(favorites, userID)
    }
}