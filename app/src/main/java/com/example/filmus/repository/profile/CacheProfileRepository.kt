package com.example.filmus.repository.profile

import com.example.filmus.domain.database.profile.ProfileDao
import com.example.filmus.domain.database.profile.ProfileEntity
import com.example.filmus.domain.profile.ProfileResponse
import kotlinx.coroutines.flow.Flow


class CacheProfileRepository(private val profileDao: ProfileDao) : ICacheProfileRepository {
    override fun getProfile(): Flow<ProfileEntity?> {
        return profileDao.getProfile()
    }

    override suspend fun cacheProfile(profile: ProfileResponse) {
        val profileEntity = ProfileEntity(
            id = profile.id,
            nickname = profile.nickname,
            email = profile.email,
            avatarLink = profile.avatarLink,
            name = profile.name,
            gender = profile.gender,
            birthDate = profile.birthDate

        )
        profileDao.insertProfile(profileEntity)
    }

    override suspend fun clearProfile() {
        profileDao.deleteProfile()
    }

    override suspend fun getProfileId(): String {
        return profileDao.getProfileId()
    }
}