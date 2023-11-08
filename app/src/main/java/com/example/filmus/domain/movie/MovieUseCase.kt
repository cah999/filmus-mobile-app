package com.example.filmus.domain.movie

import com.example.filmus.api.DetailedMovieResponse
import com.example.filmus.api.ReviewRequest
import com.example.filmus.repository.movie.MovieRepository

class MovieUseCase(private val movieRepository: MovieRepository) {
    suspend fun getMovieDetails(id: String): DetailedMovieResponse? {
        return movieRepository.getDetailedMovie(id)
    }

    suspend fun addReview(movieID: String, request: ReviewRequest) {
        movieRepository.addReview(movieID, request)
    }

    suspend fun editReview(
        movieID: String,
        reviewID: String,
        request: ReviewRequest
    ) {
        movieRepository.editReview(movieID, reviewID, request)
    }

    suspend fun removeReview(movieID: String, reviewID: String) {
        movieRepository.removeReview(movieID, reviewID)
    }

}