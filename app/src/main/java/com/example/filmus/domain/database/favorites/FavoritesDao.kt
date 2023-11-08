package com.example.filmus.domain.database.favorites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// todo что такое дао?
@Dao
interface FavoritesDao {
    @Query("SELECT movieID FROM favorites WHERE userId = :userId")
    suspend fun getUserFavorites(userId: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(favorites: List<FavoritesEntity>)

    @Query("DELETE FROM favorites WHERE userId = :userId AND movieID IN (:favorites)")
    suspend fun deleteFavorites(favorites: List<String>, userId: String)

    @Query("DELETE FROM favorites WHERE userId = :userId")
    suspend fun clearFavorites(userId: String)
}