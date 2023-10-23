package com.example.filmus.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.createApiService
import com.example.filmus.domain.login.LoginResult
import com.example.filmus.domain.login.LoginUseCase
import com.example.filmus.domain.login.TokenManager
import com.example.filmus.repository.login.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val tokenManager: TokenManager) : ViewModel() {


    fun login(username: String, password: String, onResult: (LoginResult) -> Unit) {
        viewModelScope.launch() {
            val apiService = createApiService()
            val loginRepository = LoginRepository(apiService)
            val loginUseCase = LoginUseCase(loginRepository)
            val result = loginUseCase.login(username.trim(), password.trim())

            if (result is LoginResult.Success) {
                val token = result.token
                tokenManager.saveToken(token)
            }

            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
}