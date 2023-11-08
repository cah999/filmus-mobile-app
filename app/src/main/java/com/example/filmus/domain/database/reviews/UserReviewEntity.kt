package com.example.filmus.domain.database.reviews

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_reviews")
data class UserReviewEntity(
    @PrimaryKey val reviewId: String,
    val userId: String
)