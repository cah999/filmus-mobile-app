package com.example.filmus.viewmodel.mainscreen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.createApiService
import com.example.filmus.domain.UIState
import com.example.filmus.domain.main.MainUseCase
import com.example.filmus.domain.main.Movie
import com.example.filmus.repository.main.MainRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val screenState = mutableStateOf(UIState.DEFAULT)
    val movies = mutableStateListOf<Movie>()
    private var currentPage = 1
    fun getMovies(page: Int) {
        viewModelScope.launch {
            try {
                val apiService = createApiService()
                val mainRepository = MainRepository(apiService)
                val mainUseCase = MainUseCase(mainRepository)
                val result = mainUseCase.getMovies(page)
                Log.d("MainViewModel", "getMovies: $result")
                movies.addAll(result)
            } finally {
                screenState.value = UIState.DEFAULT
            }
        }
    }

    fun loadNextPage() {
        currentPage++
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            getMovies(currentPage)
        }
    }
}