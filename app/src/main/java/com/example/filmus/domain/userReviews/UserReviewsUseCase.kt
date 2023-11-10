package com.example.filmus.domain.userReviews

import com.example.filmus.repository.userReviews.UserReviewsRepository

class UserReviewsUseCase(private val userReviewsRepository: UserReviewsRepository) {
    suspend fun getProfileReviews(userID: String): List<String> {
        return userReviewsRepository.getProfileReviews(userID = userID)
    }

    suspend fun addReview(userID: String, review: String) {
        userReviewsRepository.addReview(userID = userID, review = review)
    }

    suspend fun removeReview(review: String) {
        userReviewsRepository.removeReview(review = review)
    }
}