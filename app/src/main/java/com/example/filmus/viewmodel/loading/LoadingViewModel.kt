package com.example.filmus.viewmodel.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.createApiService
import com.example.filmus.domain.database.profile.ProfileDao
import com.example.filmus.domain.database.profile.ProfileDatabase
import com.example.filmus.domain.profile.CacheProfileUseCase
import com.example.filmus.domain.profile.ProfileResponse
import com.example.filmus.domain.profile.ProfileUseCase
import com.example.filmus.repository.TokenManager
import com.example.filmus.repository.profile.ApiProfileRepository
import com.example.filmus.repository.profile.CacheProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadingViewModel(private val tokenManager: TokenManager) : ViewModel() {

    fun checkUser(onResult: (ApiResult<Nothing>) -> Unit) {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token != null) {
                val apiService = createApiService(token)
                val profileRepository = ApiProfileRepository(apiService)
                val profileUseCase = ProfileUseCase(profileRepository)

                when (val result = profileUseCase.getInfo()) {
                    is ApiResult.Success -> {
                        if (result.data != null) {
                            cacheProfile(result.data)
                            withContext(Dispatchers.Main) {
                                onResult(ApiResult.Success())

                            }
                        }
                    }

                    is ApiResult.Unauthorized -> {
                        tokenManager.clearToken()
                        withContext(Dispatchers.Main) {
                            onResult(ApiResult.Unauthorized())
                        }
                    }

                    is ApiResult.Error -> {
                        withContext(Dispatchers.Main) {
                            onResult(ApiResult.Error(result.message))
                        }
                    }
                }
            } else {
                onResult(ApiResult.Error())
            }
        }
    }

    private fun cacheProfile(profile: ProfileResponse) {
        viewModelScope.launch {
            val profileDatabase = ProfileDatabase.getDatabase(
                context = tokenManager.context
            )
            val profileDao: ProfileDao = profileDatabase.profileDao()
            val cacheProfileRepository = CacheProfileRepository(profileDao)
            val cacheProfileUseCase = CacheProfileUseCase(cacheProfileRepository)

            cacheProfileUseCase.cacheProfile(profile)
        }
    }
}