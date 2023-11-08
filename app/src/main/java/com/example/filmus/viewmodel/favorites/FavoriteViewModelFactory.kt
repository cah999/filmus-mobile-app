package com.example.filmus.viewmodel.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmus.domain.UserManager

class FavoritesViewModelFactory(
    private val userManager: UserManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(userManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}