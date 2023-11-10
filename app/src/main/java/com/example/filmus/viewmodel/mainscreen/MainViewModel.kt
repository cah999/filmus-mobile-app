package com.example.filmus.viewmodel.mainscreen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.createApiService
import com.example.filmus.domain.UIState
import com.example.filmus.domain.UserManager
import com.example.filmus.domain.main.MainUseCase
import com.example.filmus.domain.main.Movie
import com.example.filmus.repository.main.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userManager: UserManager) : ViewModel() {
    val screenState = mutableStateOf(UIState.DEFAULT)
    val movies = mutableStateListOf<Movie>()
    private var currentPage = 1
    val userReviews = mutableListOf<String>()

    init {
        updateMovies()
        getProfileReviews()
    }

    private fun getMovies(page: Int) {
        viewModelScope.launch {
            try {
                val apiService = createApiService()
                val mainRepository = MainRepository(apiService)
                val mainUseCase = MainUseCase(mainRepository)
                val result = mainUseCase.getMovies(page)
                movies.addAll(result)
            } finally {
                screenState.value = UIState.DEFAULT
            }
        }
    }

    fun loadNextPage() {
        currentPage++
        if (currentPage > 10) {
            currentPage = 10
        }
        screenState.value = UIState.REFRESHING
        viewModelScope.launch {
            getMovies(currentPage)
        }
    }

    private fun getProfileReviews() {
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            try {
                userReviews.clear()
                userManager.getProfileReviews().let { reviews ->
                    userReviews.addAll(reviews)
                }
            } finally {
                screenState.value = UIState.DEFAULT
            }
        }
    }

    fun updateMovies() {
        currentPage = 1
        screenState.value = UIState.LOADING
        viewModelScope.launch {
            try {
                movies.clear()
                val apiService = createApiService()
                val mainRepository = MainRepository(apiService)
                val mainUseCase = MainUseCase(mainRepository)
                val result = mainUseCase.getMovies(currentPage)
                Log.d("MainViewModel", "getMovies: $result")
                movies.addAll(result)
            } finally {
                screenState.value = UIState.DEFAULT
            }
            getProfileReviews()
        }
    }
}