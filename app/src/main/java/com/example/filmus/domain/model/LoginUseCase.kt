package com.example.filmus.domain.model

import com.example.filmus.repository.LoginRepository


class LoginUseCase(private val loginRepository: LoginRepository) {
    suspend fun login(username: String, password: String): LoginResult {
        return loginRepository.login(username, password)
    }
}