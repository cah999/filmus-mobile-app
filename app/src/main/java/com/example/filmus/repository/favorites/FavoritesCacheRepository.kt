package com.example.filmus.repository.favorites

import com.example.filmus.domain.database.favorites.FavoritesDao
import com.example.filmus.domain.database.favorites.FavoritesEntity

class FavoritesCacheRepository(
    private val favoritesDao: FavoritesDao
) {
    suspend fun getFavorites(userID: String): List<String> {
        return favoritesDao.getUserFavorites(userID)
    }

    suspend fun addFavorites(favorites: List<String>, userID: String) {
        favoritesDao.insertFavorites(favorites.map { movieID ->
            FavoritesEntity(
                movieID = movieID,
                userId = userID
            )
        })
    }

    suspend fun removeFavorites(favorites: List<String>, userID: String) {
        favoritesDao.deleteFavorites(favorites, userID)
    }

    suspend fun clearFavorites(userID: String?) {
        if (userID != null) favoritesDao.clearFavorites(userID)
        else favoritesDao.clearCache()
    }
}