package com.example.filmus.repository.profile

import com.example.filmus.domain.database.profile.ProfileDao
import com.example.filmus.domain.database.profile.ProfileEntity
import kotlinx.coroutines.flow.Flow

class CacheProfileRepository(private val profileDao: ProfileDao) {
    fun getProfile(): Flow<ProfileEntity?> {
        return profileDao.getProfile()
    }

    suspend fun cacheProfile(profile: ProfileEntity) {
        profileDao.insertProfile(profile)
    }

    suspend fun clearProfile() {
        profileDao.deleteProfile()
    }

    suspend fun getProfileId(): String {
        return profileDao.getProfileId()
    }
}