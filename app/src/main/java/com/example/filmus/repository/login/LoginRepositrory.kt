package com.example.filmus.repository.login

import com.example.filmus.domain.login.LoginResult


interface LoginRepository {
    suspend fun login(username: String, password: String): LoginResult
}
