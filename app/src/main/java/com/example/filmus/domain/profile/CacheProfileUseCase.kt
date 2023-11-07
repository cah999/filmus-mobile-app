package com.example.filmus.domain.profile

import com.example.filmus.domain.database.ProfileEntity
import com.example.filmus.repository.profile.CacheProfileRepository
import kotlinx.coroutines.flow.Flow

class CacheProfileUseCase(private val profileRepository: CacheProfileRepository) {
    fun getProfile(): Flow<ProfileEntity?> {
        return profileRepository.getProfile()
    }

    suspend fun cacheProfile(profile: ProfileEntity) {
        profileRepository.cacheProfile(profile)
    }

    suspend fun clearProfile() {
        profileRepository.clearProfile()
    }
}