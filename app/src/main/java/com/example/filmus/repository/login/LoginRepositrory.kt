package com.example.filmus.repository.login

import android.util.Log
import com.example.filmus.api.ApiService
import com.example.filmus.api.LoginRequest
import com.example.filmus.domain.MoshiProvider
import com.example.filmus.domain.UserManager
import com.example.filmus.domain.login.LoginResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class LoginRepository(private val apiService: ApiService, private val userManager: UserManager) {
    suspend fun login(username: String, password: String): LoginResult {
        try {
            Log.d("LoginRepository", "login: $username, $password")
            val loginRequest = LoginRequest(username, password)
            val loginRequestBody = MoshiProvider.loginRequestAdapter.toJson(loginRequest)
                .toRequestBody("application/json".toMediaTypeOrNull())
            val response = apiService.login(loginRequestBody)
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (!token.isNullOrBlank()) {
                    userManager.saveToken(token)
                    userManager.checkToken()
                    return LoginResult.Success(token)
                }
            } else if (response.code() == 400) {
                return LoginResult.Error("Неверный логин или пароль")
            }
            return LoginResult.Error("Неизвестная ошибка")
        } catch (e: Exception) {
            return LoginResult.Error("Error: ${e.message}")
        }
    }
}
