package com.example.filmus.repository.profile

import android.util.Log
import com.example.filmus.api.ApiService
import com.example.filmus.api.ProfileResponse
import com.example.filmus.api.ProfileResult
import com.example.filmus.api.ProfileUpdateResult
import com.example.filmus.domain.MoshiProvider
import com.example.filmus.domain.UserManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileRepository(
    private val apiService: ApiService,
    private val userManager: UserManager
) {
    suspend fun getInfo(): ProfileResult {
        try {
            val response = apiService.getInfo()
            Log.d("ProfileRepository", "getInfo: $response")
            if (response.isSuccessful) {
                val data = response.body()
                return if (data != null) {
                    ProfileResult.Success(data)
                } else {
                    ProfileResult.Error("Error")
                }
            } else if (response.code() == 401) {
                userManager.clearToken()
                return ProfileResult.Unauthorized("Unauthorized")
            }
        } catch (e: Exception) {
            return ProfileResult.Error(e.message ?: "Error")
        }
        return ProfileResult.Error("Error")
    }

    suspend fun logout() {
        try {
            val response = apiService.logout()
            Log.d("ProfileRepository", "logout: $response")
            if (response.isSuccessful) {
                userManager.clearToken()
            }
        } catch (e: Exception) {
            Log.d("ProfileRepository", "logout: ${e.message}")
        }
    }

    suspend fun updateInfo(data: ProfileResponse): ProfileUpdateResult {
        try {
            val profileRequestBody = MoshiProvider.profileRequestAdapter.toJson(data)
                .toRequestBody("application/json".toMediaTypeOrNull())
            val response = apiService.updateInfo(profileRequestBody)
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    return ProfileUpdateResult.Success("Success")
                }
            } else if (response.code() == 401) {
                return ProfileUpdateResult.Unauthorized("Unauthorized")
            }
            return ProfileUpdateResult.Error("Error")
        } catch (e: Exception) {
            Log.d("ProfileRepository", "updateInfo: ${e.message}")
            return ProfileUpdateResult.Error("Error")
        }
    }
}
