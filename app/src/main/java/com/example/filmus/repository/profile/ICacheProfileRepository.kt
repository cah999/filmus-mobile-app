package com.example.filmus.repository.profile

import com.example.filmus.domain.database.profile.ProfileEntity
import com.example.filmus.domain.profile.ProfileResponse
import kotlinx.coroutines.flow.Flow

interface ICacheProfileRepository {
    fun getProfile(): Flow<ProfileEntity?>
    suspend fun cacheProfile(profile: ProfileResponse)
    suspend fun clearProfile()
    suspend fun getProfileId(): String
}