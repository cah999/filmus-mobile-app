package com.example.filmus.viewmodel.registration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.RegistrationRequest
import com.example.filmus.api.createApiService
import com.example.filmus.domain.registration.RegistrationResult
import com.example.filmus.domain.registration.RegistrationUseCase
import com.example.filmus.repository.registration.RegistrationRepository
import kotlinx.coroutines.launch


// todo почитать про state, mvi
class RegistrationViewModel() : ViewModel() {
    val name = mutableStateOf("")
    val gender = mutableStateOf(true)
    val login = mutableStateOf("")
    val email = mutableStateOf("")
    val birthDate = mutableStateOf("")
    val password = mutableStateOf("")
    val passwordRepeat = mutableStateOf("")

    fun register(onResult: (RegistrationResult) -> Unit) {
        viewModelScope.launch() {
            val apiService = createApiService()
            val registrationRepository = RegistrationRepository(apiService)
            val registrationUseCase = RegistrationUseCase(registrationRepository)
            val result = registrationUseCase.register(
                RegistrationRequest(
                    name = name.value,
                    login = login.value,
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