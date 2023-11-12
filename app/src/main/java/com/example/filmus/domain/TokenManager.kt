package com.example.filmus.domain

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TokenManager(val context: Context) {
    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

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

    fun getToken(): String? {
        return try {
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "token_prefs",
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            sharedPreferences.getString("token", null)
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
}