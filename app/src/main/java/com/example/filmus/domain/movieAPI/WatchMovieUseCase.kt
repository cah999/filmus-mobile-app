package com.example.filmus.domain.movieAPI

import com.example.filmus.domain.api.ApiResult
import com.example.filmus.repository.movie.watchMovie.WatchMovieRepository

class WatchMovieUseCase(private val repository: WatchMovieRepository) {

    suspend fun searchMovie(query: String): ApiResult<String?> {
        return repository.search(query)
    }

    suspend fun getMovieDetails(path: String): ApiResult<ExMovie> {
        return repository.getMovieDetails(path)
    }

    suspend fun loadMovieResolutions(id: Int, translationId: Int, season: Int?, episode: Int?): ApiResult<List<Stream>> {
        return repository.loadMovieResolutions(id, translationId, season, episode)
    }

    suspend fun loadSeasonsForTranslation(id: Int, translationId: Int): ApiResult<List<Season>> {
        return repository.loadSeasonsForTranslation(id, translationId)
    }

    suspend fun getMovieTrailer(id: Int): ApiResult<String> {
        return repository.getMovieTrailer(id)
    }
}