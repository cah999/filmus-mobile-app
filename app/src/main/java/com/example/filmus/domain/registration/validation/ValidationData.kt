package com.example.filmus.domain.registration.validation

data class ValidationData(
    val name: String,
    val login: String?,
    val email: String,
    val birthDate: String
)