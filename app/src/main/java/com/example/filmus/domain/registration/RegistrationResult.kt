package com.example.filmus.domain.registration

sealed class RegistrationResult {
    data class Success(val token: String) : RegistrationResult()
    data class Error(val message: String, val errors: String) : RegistrationResult()
}