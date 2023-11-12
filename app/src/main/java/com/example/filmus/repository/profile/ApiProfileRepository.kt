package com.example.filmus.repository.profile

import com.example.filmus.common.Constants
import com.example.filmus.domain.MoshiProvider
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.ApiService
import com.example.filmus.domain.profile.ProfileResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ApiProfileRepository(
    private val apiService: ApiService,
) {
    suspend fun getInfo(): ApiResult<ProfileResponse> {
        try {
            val response = apiService.getInfo()
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    return ApiResult.Success(data)
                }
            } else if (response.code() == 401) {
                return ApiResult.Unauthorized()
            }
        } catch (e: Exception) {
            return ApiResult.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
        return ApiResult.Error(Constants.UNKNOWN_ERROR)
    }

    suspend fun logout(): ApiResult<Nothing> {
        try {
            val response = apiService.logout()
            if (response.isSuccessful) {
                return ApiResult.Success()
            } else if (response.code() == 401) {
                return ApiResult.Unauthorized()
            }
        } catch (e: Exception) {
            return ApiResult.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
        return ApiResult.Error(Constants.UNKNOWN_ERROR)
    }

    suspend fun updateInfo(data: ProfileResponse): ApiResult<Nothing> {
        try {
            val profileRequestBody = MoshiProvider.profileRequestAdapter.toJson(data)
                .toRequestBody("application/json".toMediaTypeOrNull())
            val response = apiService.updateInfo(profileRequestBody)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    return ApiResult.Success()
                }
            } else if (response.code() == 401) {
                return ApiResult.Unauthorized()
            }
            return ApiResult.Error(Constants.UNKNOWN_ERROR)
        } catch (e: Exception) {
            return ApiResult.Error(e.message ?: Constants.UNKNOWN_ERROR)
        }
    }
}
