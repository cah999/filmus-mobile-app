package com.example.filmus.repository.userReviews

import com.example.filmus.domain.database.reviews.UserReviewDao
import com.example.filmus.domain.database.reviews.UserReviewEntity

class UserReviewsRepository(private val userReviewDao: UserReviewDao) {
    suspend fun getProfileReviews(
        userID: String
    ): List<String> {
        return userReviewDao.getUserReviews(userID)
    }

    suspend fun addReview(
        userID: String,
        review: String
    ) {
        userReviewDao.insertUserReview(UserReviewEntity(review, userID))
    }

    suspend fun removeReview(
        review: String
    ) {
        userReviewDao.deleteUserReview(review)
    }

    suspend fun clearReviews() {
        userReviewDao.clearReviews()
    }
}