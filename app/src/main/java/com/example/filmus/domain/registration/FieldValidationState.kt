package com.example.filmus.domain.registration

data class FieldValidationState(
    var isValid: Boolean,
    val errorMessage: String
)