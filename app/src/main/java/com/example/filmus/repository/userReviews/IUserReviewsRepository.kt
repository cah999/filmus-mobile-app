package com.example.filmus.repository.userReviews

interface IUserReviewsRepository {
    suspend fun getProfileReviews(
        userID: String
    ): List<String>

    suspend fun addReview(
        userID: String,
        review: String
    )

    suspend fun removeReview(
        review: String
    )

    suspend fun clearReviews()
}