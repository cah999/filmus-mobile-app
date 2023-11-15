package com.example.filmus.domain.login

import com.example.filmus.domain.api.ApiResult
import com.example.filmus.repository.login.LoginRepository


class LoginUseCase(private val loginRepository: LoginRepository) {
    suspend fun login(username: String, password: String): ApiResult<LoginResponse> {
        return loginRepository.login(username, password)
    }
}