package com.example.filmus.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.domain.TokenManager
import kotlinx.coroutines.launch

class ProfileViewModel(private val tokenManager: TokenManager) : ViewModel() {

    fun logout() {
        viewModelScope.launch() {
            tokenManager.clearToken()
        }
    }
}