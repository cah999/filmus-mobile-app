package com.example.filmus.domain.profile

import com.example.filmus.domain.api.ApiResult
import com.example.filmus.repository.profile.ApiProfileRepository

class ProfileUseCase(private val profileRepository: ApiProfileRepository) {
    suspend fun getInfo(): ApiResult<ProfileResponse> {
        return profileRepository.getInfo()
    }

    suspend fun logout(): ApiResult<Nothing> {
        return profileRepository.logout()
    }

    suspend fun updateInfo(data: ProfileResponse): ApiResult<Nothing> {
        return profileRepository.updateInfo(data)
    }
}