package com.example.filmus.domain

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.filmus.api.ProfileResponse
import com.example.filmus.api.ProfileResult
import com.example.filmus.api.createApiService
import com.example.filmus.api.toProfileEntity
import com.example.filmus.domain.database.ProfileDatabase
import com.example.filmus.domain.database.ProfileEntity
import com.example.filmus.domain.profile.CacheProfileUseCase
import com.example.filmus.domain.profile.ProfileUseCase
import com.example.filmus.repository.profile.CacheProfileRepository
import com.example.filmus.repository.profile.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserManager(private val context: Context) {
    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val profileDatabase = ProfileDatabase.getDatabase(context)
    private val profileDao = profileDatabase.profileDao()

    suspend fun saveToken(token: String) {
        try {
            withContext(Dispatchers.IO) {
                val sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    "token_prefs",
                    masterKeyAlias,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
                sharedPreferences.edit().putString("token", token).apply()
            }
        } catch (e: Exception) {
            Log.e("TokenManager", "Error saving token", e)
        }
    }

    suspend fun getToken(): String? {
        return try {
            withContext(Dispatchers.IO) {
                val sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    "token_prefs",
                    masterKeyAlias,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
                sharedPreferences.getString("token", null)
            }
        } catch (e: Exception) {
            Log.e("TokenManager", "Error getting token", e)
            null
        }
    }

    suspend fun clearToken() {
        try {
            withContext(Dispatchers.IO) {
                val sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    "token_prefs",
                    masterKeyAlias,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
                sharedPreferences.edit().clear().apply()
            }
        } catch (e: Exception) {
            Log.e("TokenManager", "Error clearing token", e)
        }
    }


    suspend fun getProfile(): Flow<ProfileEntity?> {
        val getProfileUseCase = CacheProfileUseCase(CacheProfileRepository(profileDao))
        return getProfileUseCase.getProfile()
    }

    suspend fun cacheProfile(profile: ProfileResponse) {
        val token = getToken()
        if (token != null) {
            val getProfileUseCase = CacheProfileUseCase(CacheProfileRepository(profileDao))
            getProfileUseCase.cacheProfile(profile.toProfileEntity())
        }
    }

    suspend fun clearCache() {
        val getProfileUseCase = CacheProfileUseCase(CacheProfileRepository(profileDao))
        getProfileUseCase.clearProfile()
        Log.d("UserManager", "clearCache: called")
    }

    suspend fun checkToken(): Boolean {
        val token = getToken()
        if (token != null) {
            val apiService = createApiService(token)
            val profileRepository = ProfileRepository(apiService, this)
            val profileUseCase = ProfileUseCase(profileRepository)

            when (val result = profileUseCase.getInfo()) {
                is ProfileResult.Success -> {
                    Log.d("UserManager", "checkToken: result: ${result.profile}")
                    cacheProfile(result.profile)
                    return true
                }

                is ProfileResult.Unauthorized -> {
                    clearToken()
                }

                else -> {
                    Log.d("UserManager", "checkToken: result: $result")
                }
            }
        }
        return false
    }
}
