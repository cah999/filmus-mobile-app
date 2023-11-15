package com.example.filmus.repository.movie

import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.movie.DetailedMovieResponse
import com.example.filmus.domain.movie.ReviewRequest

interface IMovieRepository {
    suspend fun getDetailedMovie(id: String): ApiResult<DetailedMovieResponse?>
    suspend fun addReview(movieID: String, request: ReviewRequest): ApiResult<Nothing>
    suspend fun editReview(
        movieID: String, reviewID: String, request: ReviewRequest
    ): ApiResult<Nothing>

    suspend fun removeReview(movieID: String, reviewID: String): ApiResult<Nothing>
}