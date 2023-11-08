package com.example.filmus.viewmodel.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmus.domain.UserManager

class MovieViewModelFactory(
    private val movieId: String,
    private val userManager: UserManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(movieId = movieId, userManager = userManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}