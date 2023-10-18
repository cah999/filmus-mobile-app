package com.example.filmus.viewmodel.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.createApiService
import com.example.filmus.domain.model.LoginResult
import com.example.filmus.domain.model.LoginUseCase
import com.example.filmus.repository.ApiLoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel() : ViewModel() {

    fun register(username: String, password: String, onResult: (LoginResult) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiService = createApiService()
            val loginRepository = ApiLoginRepository(apiService)
            val loginUseCase = LoginUseCase(loginRepository)
            val result = loginUseCase.login(username, password)
            onResult(result)
        }
    }
}