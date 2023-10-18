package com.example.filmus.viewmodel.registration

import android.text.format.DateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.createApiService
import com.example.filmus.domain.model.LoginResult
import com.example.filmus.domain.model.LoginUseCase
import com.example.filmus.repository.ApiLoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class RegistrationData(
    val username: String,
    val gender: Boolean,
    val login: String,
    val email: String,
    val birthDate: String,
    val password: String
)

class RegistrationViewModel() : ViewModel() {
    private var registrationData: RegistrationData? = null

    fun setRegistrationData(data: RegistrationData) {
        registrationData = data
    }

    fun getRegistrationData(): RegistrationData? {
        return registrationData
    }

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