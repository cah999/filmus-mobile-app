package com.example.filmus.domain.movie

import com.example.filmus.api.DetailedMovieResponse
import com.example.filmus.repository.movie.MovieRepository

class MovieUseCase(private val movieRepository: MovieRepository) {
    suspend fun getMovieDetails(id: String): DetailedMovieResponse? {
        return movieRepository.getDetailedMovie(id)
    }
}