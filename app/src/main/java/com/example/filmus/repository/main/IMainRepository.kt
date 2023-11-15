package com.example.filmus.repository.main

import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.main.Movie

interface IMainRepository {
    suspend fun getMovies(page: Int): ApiResult<List<Movie>>
}