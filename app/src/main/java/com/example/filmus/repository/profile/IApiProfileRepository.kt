package com.example.filmus.repository.profile

import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.profile.ProfileResponse

interface IApiProfileRepository {
    suspend fun getInfo(): ApiResult<ProfileResponse>
    suspend fun logout(): ApiResult<Nothing>
    suspend fun updateInfo(data: ProfileResponse): ApiResult<Nothing>
}