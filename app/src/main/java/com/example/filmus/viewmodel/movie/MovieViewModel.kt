package com.example.filmus.viewmodel.movie

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.DetailedMovieResponse
import com.example.filmus.api.createApiService
import com.example.filmus.domain.movie.MovieUseCase
import com.example.filmus.repository.movie.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(movieId: String) : ViewModel() {
    private val movieId = movieId
    var movieDetails = mutableStateOf(null as DetailedMovieResponse?)
    suspend fun getMovieDetails() {
        viewModelScope.launch {
            val apiService = createApiService()
            val movieRepository = MovieRepository(apiService)
            val movieUseCase = MovieUseCase(movieRepository)
            val result = movieUseCase.getMovieDetails(movieId)
            Log.d("MainViewModel", "getMovies: $result")
            if (result != null) movieDetails.value = result
        }
    }
}