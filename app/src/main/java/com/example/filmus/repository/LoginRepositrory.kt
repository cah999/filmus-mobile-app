package com.example.filmus.repository

import android.util.Log
import com.example.filmus.api.ApiService
import com.example.filmus.api.LoginRequest
import com.example.filmus.domain.model.LoginResult
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

object MoshiProvider {
    val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val loginRequestAdapter: JsonAdapter<LoginRequest> = moshi.adapter(LoginRequest::class.java)
}

interface LoginRepository {
    suspend fun login(username: String, password: String): LoginResult
}


class ApiLoginRepository(private val apiService: ApiService) : LoginRepository {
    override suspend fun login(username: String, password: String): LoginResult {
        try {
            Log.d("LoginRepository", "login: $username, $password")
            val loginRequest = LoginRequest(username, password)
            val loginRequestBody = MoshiProvider.loginRequestAdapter.toJson(loginRequest)
                .toRequestBody("application/json".toMediaTypeOrNull())
            val response = apiService.login(loginRequestBody)
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (!token.isNullOrBlank()) {
                    return LoginResult.Success(token)
                }
            }
            return LoginResult.Error("Login failed")
        } catch (e: Exception) {
            return LoginResult.Error("Error: ${e.message}")
        }
    }
}