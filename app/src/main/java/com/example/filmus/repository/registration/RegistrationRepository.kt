package com.example.filmus.repository.registration

import android.util.Log
import com.example.filmus.api.ApiService
import com.example.filmus.api.RegistrationRequest
import com.example.filmus.domain.MoshiProvider
import com.example.filmus.domain.TokenManager
import com.example.filmus.domain.registration.register.RegistrationResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class RegistrationRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun register(data: RegistrationRequest): RegistrationResult {
        try {
            Log.d("RegistrationRepository", "registration: $data")
            val registerRequestBody = MoshiProvider.registerRequestAdapter.toJson(data)
                .toRequestBody("application/json".toMediaTypeOrNull())
            val response = apiService.register(registerRequestBody)
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (!token.isNullOrBlank()) {
                    tokenManager.saveToken(token)
                    return RegistrationResult.Success(token)

                }
            }
            return RegistrationResult.Error(
                "Registration failed",
                response.errorBody()?.string() ?: ""
            )
        } catch (e: Exception) {
            return RegistrationResult.Error("Error: ${e.message}", "")
        }
    }
}