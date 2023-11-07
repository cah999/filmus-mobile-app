package com.example.filmus.viewmodel.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.createApiService
import com.example.filmus.domain.UIState
import com.example.filmus.domain.UserManager
import com.example.filmus.domain.login.LoginResult
import com.example.filmus.domain.login.LoginUseCase
import com.example.filmus.repository.login.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val userManager: UserManager) : ViewModel() {
    var username = mutableStateOf("")
    var password = mutableStateOf("")
    var state = mutableStateOf(UIState.DEFAULT)
    var errorMessage = mutableStateOf("")

    fun login(username: String, password: String, onResult: (LoginResult) -> Unit) {
        viewModelScope.launch() {
            val apiService = createApiService()
            val loginRepository = LoginRepository(apiService, userManager)
            val loginUseCase = LoginUseCase(loginRepository)
            val result = loginUseCase.login(username, password)
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
}