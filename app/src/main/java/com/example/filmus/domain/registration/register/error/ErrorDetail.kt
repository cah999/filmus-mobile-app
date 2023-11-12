package com.example.filmus.domain.registration.register.error

data class ErrorDetail(
    val rawValue: Any?,
    val attemptedValue: Any?,
    val errors: List<Error>,
    val validationState: Int,
    val isContainerNode: Boolean,
    val children: Any?
)