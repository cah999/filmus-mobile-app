package com.example.filmus.repository.favorites

import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.main.Movie

interface IFavoritesRepository {
    suspend fun getFavorites(): ApiResult<List<Movie>>
    suspend fun addFavorite(movieID: String): ApiResult<Nothing>
    suspend fun removeFavorite(movieID: String): ApiResult<Nothing>
}