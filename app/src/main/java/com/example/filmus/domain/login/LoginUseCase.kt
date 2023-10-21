package com.example.filmus.domain.login

import com.example.filmus.repository.login.LoginRepository


class LoginUseCase(private val loginRepository: LoginRepository) {
    suspend fun login(username: String, password: String): LoginResult {
        return loginRepository.login(username, password)
    }
}