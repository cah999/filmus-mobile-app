package com.example.filmus.repository.movie.watchMovie

import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.movieAPI.Api
import com.example.filmus.domain.movieAPI.ExMovie
import com.example.filmus.domain.movieAPI.Season
import com.example.filmus.domain.movieAPI.Stream

class WatchMovieRepository(private val api: Api) {

    suspend fun search(query: String): ApiResult<String?> {
        return try {
            val response = api.searchAjax(query)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message.toString())
        }
    }

    suspend fun getMovieDetails(path: String): ApiResult<ExMovie> {
        return try {
            val response = api.getMovie(path)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message.toString())
        }
    }

    suspend fun loadMovieResolutions(
        movieID: Int,
        translationId: Int,
        season: Int?,
        episode: Int?
    ): ApiResult<List<Stream>> {
        return try {
            val response = api.loadResolutions(
                movieID,
                translationId = translationId,
                season = season,
                episode = episode
            )
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message.toString())
        }
    }

    suspend fun loadSeasonsForTranslation(
        movieID: Int,
        translationId: Int
    ): ApiResult<List<Season>> {
        return try {
            val response = api.loadSeasonsForTranslation(movieID, translationId)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message.toString())
        }
    }

    suspend fun getMovieTrailer(movieID: Int): ApiResult<String> {
        return try {
            val response = api.getTrailer(movieID)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message.toString())
        }
    }

}