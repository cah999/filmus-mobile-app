package com.example.filmus.domain.database.favorites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoritesEntity(
    @PrimaryKey val movieID: String,
    val userId: String
)