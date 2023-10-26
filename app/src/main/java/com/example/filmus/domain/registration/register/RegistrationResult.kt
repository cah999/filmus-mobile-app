package com.example.filmus.domain.registration.register

sealed class RegistrationResult {
    data class Success(val token: String) : RegistrationResult()
    data class Error(val message: String, val errors: String) : RegistrationResult()
}