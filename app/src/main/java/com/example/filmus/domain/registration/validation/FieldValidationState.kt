package com.example.filmus.domain.registration.validation

data class FieldValidationState(
    var isValid: Boolean,
    val errorMessage: String
)