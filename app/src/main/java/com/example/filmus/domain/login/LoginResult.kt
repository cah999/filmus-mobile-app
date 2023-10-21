package com.example.filmus.domain.login

sealed class LoginResult {
    data class Success(val token: String) : LoginResult()
    data class Error(val message: String) : LoginResult()
}
