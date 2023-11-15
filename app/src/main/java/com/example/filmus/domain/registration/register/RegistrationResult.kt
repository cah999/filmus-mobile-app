package com.example.filmus.domain.registration.register

import com.example.filmus.domain.registration.register.error.RegistrationErrorDetails

sealed class RegistrationResult {
    data class Success(val token: String) : RegistrationResult()
    data class Error(val message: String, val errorDetails: RegistrationErrorDetails?) :
        RegistrationResult()
}
