package com.example.filmus.domain.database.reviews

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserReviewDao {
    @Query("SELECT reviewId FROM user_reviews WHERE userId = :userId")
    suspend fun getUserReviews(userId: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserReview(userReview: UserReviewEntity)

    @Query("DELETE FROM user_reviews WHERE reviewId = :reviewId")
    suspend fun deleteUserReview(reviewId: String)

    @Query("DELETE FROM user_reviews")
    suspend fun clearReviews()
}