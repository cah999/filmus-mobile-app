package com.example.filmus.domain.profile

import com.example.filmus.api.ProfileResponse
import com.example.filmus.api.ProfileResult
import com.example.filmus.api.ProfileUpdateResult
import com.example.filmus.repository.profile.ProfileRepository

class ProfileUseCase(private val profileRepository: ProfileRepository) {
    suspend fun getInfo(): ProfileResult {
        return profileRepository.getInfo()
    }

    suspend fun logout() {
        profileRepository.logout()
    }

    suspend fun updateInfo(data: ProfileResponse): ProfileUpdateResult {
        return profileRepository.updateInfo(data)
    }

}