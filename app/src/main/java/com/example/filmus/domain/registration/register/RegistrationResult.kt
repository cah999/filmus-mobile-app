package com.example.filmus.domain.registration.register

sealed class RegistrationResult {
    data class Success(val token: String) : RegistrationResult()
    data class Error(val message: String, val errorDetails: RegistrationErrorDetails?) :
        RegistrationResult()

}

data class RegistrationErrorDetails(
    val message: String,
    val errors: Map<String, ErrorDetail>
)

data class ErrorDetail(
    val rawValue: Any?,
    val attemptedValue: Any?,
    val errors: List<Error>,
    val validationState: Int,
    val isContainerNode: Boolean,
    val children: Any?
)

data class Error(
    val exception: Any?,
    val errorMessage: String
)