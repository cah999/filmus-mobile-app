package com.example.filmus.repository.userReviews

import com.example.filmus.domain.database.reviews.UserReviewDao
import com.example.filmus.domain.database.reviews.UserReviewEntity


class UserReviewsRepository(private val userReviewDao: UserReviewDao) : IUserReviewsRepository {
    override suspend fun getProfileReviews(
        userID: String
    ): List<String> {
        return userReviewDao.getUserReviews(userID)
    }

    override suspend fun addReview(
        userID: String,
        review: String
    ) {
        userReviewDao.insertUserReview(UserReviewEntity(review, userID))
    }

    override suspend fun removeReview(
        review: String
    ) {
        userReviewDao.deleteUserReview(review)
    }

    override suspend fun clearReviews() {
        userReviewDao.clearReviews()
    }
}