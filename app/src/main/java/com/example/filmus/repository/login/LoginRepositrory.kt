package com.example.filmus.repository.login

import com.example.filmus.common.Constants
import com.example.filmus.domain.MoshiProvider
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.ApiService
import com.example.filmus.domain.login.LoginRequest
import com.example.filmus.domain.login.LoginResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class LoginRepository(private val apiService: ApiService) {
    suspend fun login(username: String, password: String): ApiResult<LoginResponse> {
        try {
            val loginRequest = LoginRequest(username, password)
            val loginRequestBody = MoshiProvider.loginRequestAdapter.toJson(loginRequest)
                .toRequestBody("application/json".toMediaTypeOrNull())
            val response = apiService.login(loginRequestBody)
            when {
                response.isSuccessful -> {
                    val token = response.body()?.token
                    if (!token.isNullOrBlank()) {
                        return ApiResult.Success(LoginResponse(token))
                    }
                }

                response.code() == 400 -> {
                    return ApiResult.Error(Constants.LOGIN_ERROR)
                }
            }
        } catch (e: Exception) {
            return ApiResult.Error(Constants.UNKNOWN_ERROR)
        }
        return ApiResult.Error(Constants.UNKNOWN_ERROR)
    }
}

