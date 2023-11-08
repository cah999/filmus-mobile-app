package com.example.filmus.viewmodel.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmus.domain.UserManager

class MainViewModelFactory(
    private val userManager: UserManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}