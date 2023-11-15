package com.example.filmus.repository.movie.watchMovie

import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.movieAPI.ExMovie
import com.example.filmus.domain.movieAPI.Season
import com.example.filmus.domain.movieAPI.Stream

interface IWatchMovieRepository {
    suspend fun search(query: String): ApiResult<String?>
    suspend fun getMovieDetails(path: String): ApiResult<ExMovie>
    suspend fun loadMovieResolutions(
        movieID: Int,
        translationId: Int,
        season: Int?,
        episode: Int?
    ): ApiResult<List<Stream>>

    suspend fun loadSeasonsForTranslation(
        movieID: Int,
        translationId: Int
    ): ApiResult<List<Season>>

    suspend fun getMovieTrailer(movieID: Int): ApiResult<String>
}