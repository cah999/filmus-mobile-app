package com.example.filmus.repository.login

import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.login.LoginResponse

interface ILoginRepository {
    suspend fun login(username: String, password: String): ApiResult<LoginResponse>
}
