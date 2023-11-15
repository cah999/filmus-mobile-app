package com.example.filmus.domain.movie


import com.example.filmus.domain.api.ApiResult
import com.example.filmus.repository.movie.MovieRepository

class MovieUseCase(private val movieRepository: MovieRepository) {
    suspend fun getMovieDetails(id: String): ApiResult<DetailedMovieResponse?> {
        return movieRepository.getDetailedMovie(id)
    }

    suspend fun addReview(movieID: String, request: ReviewRequest): ApiResult<Nothing> {
        return movieRepository.addReview(movieID, request)
    }

    suspend fun editReview(
        movieID: String, reviewID: String, request: ReviewRequest
    ): ApiResult<Nothing> {
        return movieRepository.editReview(movieID, reviewID, request)
    }

    suspend fun removeReview(movieID: String, reviewID: String): ApiResult<Nothing> {
        return movieRepository.removeReview(movieID, reviewID)
    }

}