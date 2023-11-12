package com.example.filmus.domain.registration.register.error

data class RegistrationErrorDetails(
    val message: String,
    val errors: Map<String, ErrorDetail>
)