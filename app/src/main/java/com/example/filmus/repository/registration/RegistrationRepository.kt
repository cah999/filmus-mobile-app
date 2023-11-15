package com.example.filmus.repository.registration

import com.example.filmus.common.Constants
import com.example.filmus.domain.MoshiProvider
import com.example.filmus.domain.api.ApiService
import com.example.filmus.domain.registration.register.RegistrationRequest
import com.example.filmus.domain.registration.register.RegistrationResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class RegistrationRepository(
    private val apiService: ApiService
) {
    suspend fun register(data: RegistrationRequest): RegistrationResult {
        try {
            val registerRequestBody = MoshiProvider.registerRequestAdapter.toJson(data)
                .toRequestBody("application/json".toMediaTypeOrNull())
            val response = apiService.register(registerRequestBody)
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (!token.isNullOrBlank()) {
                    return RegistrationResult.Success(token)
                }
            }
            val errorDetails = response.errorBody()?.let { errorBody ->
                try {
                    MoshiProvider.registrationErrorAdapter.fromJson(errorBody.string())
                } catch (e: Exception) {
                    null
                }
            }

            return RegistrationResult.Error(
                Constants.UNKNOWN_ERROR,
                errorDetails
            )
        } catch (e: Exception) {
            return RegistrationResult.Error(
                "Error: ${e.message}",
                null
            )
        }
    }
}