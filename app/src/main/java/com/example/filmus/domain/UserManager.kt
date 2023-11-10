package com.example.filmus.domain

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.filmus.api.ProfileResponse
import com.example.filmus.api.ProfileResult
import com.example.filmus.api.createApiService
import com.example.filmus.api.toProfileEntity
import com.example.filmus.domain.database.favorites.FavoritesDatabase
import com.example.filmus.domain.database.profile.ProfileDatabase
import com.example.filmus.domain.database.profile.ProfileEntity
import com.example.filmus.domain.database.reviews.UserReviewDatabase
import com.example.filmus.domain.favorite.FavoritesCacheUseCase
import com.example.filmus.domain.profile.CacheProfileUseCase
import com.example.filmus.domain.profile.ProfileUseCase
import com.example.filmus.domain.userReviews.UserReviewsUseCase
import com.example.filmus.repository.favorites.FavoritesCacheRepository
import com.example.filmus.repository.profile.CacheProfileRepository
import com.example.filmus.repository.profile.ProfileRepository
import com.example.filmus.repository.userReviews.UserReviewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// todo cache manager
class UserManager(private val context: Context) {
    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val profileDatabase = ProfileDatabase.getDatabase(context)
    private val profileDao = profileDatabase.profileDao()
    private val userReviewDatabase = UserReviewDatabase.getDatabase(context)
    private val userReviewDao = userReviewDatabase.userReviewDao()
    private val favoritesDatabase = FavoritesDatabase.getDatabase(context)
    private val favoritesDao = favoritesDatabase.favoritesDao()

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

    suspend fun getFavorites(): List<String> {
        val favoritesUseCase = FavoritesCacheUseCase(FavoritesCacheRepository(favoritesDao))
        return favoritesUseCase.getFavorites(getProfileID())
    }

    suspend fun addFavorites(favorites: List<String>, clearOld: Boolean = false) {
        val favoritesUseCase = FavoritesCacheUseCase(FavoritesCacheRepository(favoritesDao))
        favoritesUseCase.addFavorites(favorites, getProfileID(), clearOld)
    }

    suspend fun removeFavorites(favorites: List<String>) {
        val favoritesUseCase = FavoritesCacheUseCase(FavoritesCacheRepository(favoritesDao))
        favoritesUseCase.removeFavorites(favorites, getProfileID())
    }

    suspend fun getProfile(): Flow<ProfileEntity?> {
        val profileUseCase = CacheProfileUseCase(CacheProfileRepository(profileDao))
        return profileUseCase.getProfile()
    }

    suspend fun getProfileID(): String {
        val profileUseCase = CacheProfileUseCase(CacheProfileRepository(profileDao))
        return profileUseCase.getProfileId()
    }

    suspend fun getProfileReviews(): List<String> {
        val userReviewsUserCase = UserReviewsUseCase(UserReviewsRepository(userReviewDao))
        return userReviewsUserCase.getProfileReviews(getProfileID())
    }

    suspend fun addUserReview(userID: String, reviewID: String) {
        val userReviewsUserCase = UserReviewsUseCase(UserReviewsRepository(userReviewDao))
        userReviewsUserCase.addReview(userID, reviewID)
    }

    suspend fun removeProfileReview(reviewID: String) {
        val userReviewsUserCase = UserReviewsUseCase(UserReviewsRepository(userReviewDao))
        userReviewsUserCase.removeReview(reviewID)
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
