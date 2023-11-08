package com.example.filmus.repository.userReviews

import android.util.Log
import com.example.filmus.domain.database.reviews.UserReviewDao
import com.example.filmus.domain.database.reviews.UserReviewEntity

class UserReviewsRepository(private val userReviewDao: UserReviewDao) {
    suspend fun getProfileReviews(
        userID: String
    ): List<String> {
        val res = userReviewDao.getUserReviews(userID)
        Log.d("UserReviewsRepository", "getProfileReviews: $res")
        return res
    }

    suspend fun addReview(
        userID: String,
        review: String
    ) {
        userReviewDao.insertUserReview(UserReviewEntity(review, userID))
    }
}