package com.example.filmus.viewmodel.registration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.RegistrationRequest
import com.example.filmus.api.createApiService
import com.example.filmus.domain.registration.RegistrationResult
import com.example.filmus.domain.registration.RegistrationUseCase
import com.example.filmus.repository.registration.RegistrationRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// todo почитать про state, mvi
class RegistrationViewModel() : ViewModel() {
    val username = mutableStateOf("")
    val gender = mutableStateOf(false)
    val login = mutableStateOf("")
    val email = mutableStateOf("")
    val birthDate = mutableStateOf("")
    val password = mutableStateOf("")
    val passwordRepeat = mutableStateOf("")

    fun register(onResult: (RegistrationResult) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiService = createApiService()
            val registrationRepository = RegistrationRepositoryImpl(apiService)
            val registrationUseCase = RegistrationUseCase(registrationRepository)
            val result = registrationUseCase.register(
                RegistrationRequest(
                    username = username.value,
                    name = login.value,
                    password = password.value,
                    email = email.value,
                    birthDate = birthDate.value,
                    gender = gender.value
                )
            )
            onResult(result)
        }
    }
}