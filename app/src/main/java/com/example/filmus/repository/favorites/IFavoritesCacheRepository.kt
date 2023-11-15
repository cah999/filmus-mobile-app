package com.example.filmus.repository.favorites

interface IFavoritesCacheRepository {
    suspend fun getFavorites(userID: String): List<String>
    suspend fun addFavorites(favorites: List<String>, userID: String)
    suspend fun removeFavorites(favorites: List<String>, userID: String)
    suspend fun clearFavorites(userID: String?)
}