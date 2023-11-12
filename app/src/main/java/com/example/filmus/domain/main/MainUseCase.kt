package com.example.filmus.domain.main

import com.example.filmus.domain.api.ApiResult
import com.example.filmus.repository.main.MainRepository

class MainUseCase(private val mainRepository: MainRepository) {
    suspend fun getMovies(page: Int): ApiResult<List<Movie>> {
        return mainRepository.getMovies(page)
    }
}