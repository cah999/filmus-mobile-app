package com.example.filmus.viewmodel.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.common.Constants
import com.example.filmus.domain.UIState
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.createApiService
import com.example.filmus.domain.database.profile.ProfileDatabase
import com.example.filmus.domain.login.LoginResponse
import com.example.filmus.domain.login.LoginUseCase
import com.example.filmus.domain.profile.CacheProfileUseCase
import com.example.filmus.domain.profile.ProfileResponse
import com.example.filmus.domain.profile.ProfileUseCase
import com.example.filmus.repository.TokenManager
import com.example.filmus.repository.login.LoginRepository
import com.example.filmus.repository.profile.ApiProfileRepository
import com.example.filmus.repository.profile.CacheProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val tokenManager: TokenManager) : ViewModel() {
    var username = mutableStateOf(Constants.EMPTY)
    var password = mutableStateOf(Constants.EMPTY)
    var state = mutableStateOf(UIState.DEFAULT)
    var errorMessage = mutableStateOf(Constants.EMPTY)

    fun login(username: String, password: String, onResult: (ApiResult<LoginResponse>) -> Unit) {
        viewModelScope.launch {
            val apiService = createApiService()
            val loginRepository = LoginRepository(apiService)
            val loginUseCase = LoginUseCase(loginRepository)
            val result = loginUseCase.login(username, password)
            if (result is ApiResult.Success) {
                tokenManager.saveToken(result.data?.token ?: Constants.EMPTY)
                getProfile()
            }
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }

    private suspend fun getProfile() {
        val apiService = createApiService(tokenManager.getToken())
        val profileRepository = ApiProfileRepository(apiService)
        val profileUseCase = ProfileUseCase(profileRepository)

        when (val result = profileUseCase.getInfo()) {
            is ApiResult.Success -> {
                if (result.data != null) {
                    cacheProfile(result.data)
                }
            }

            is ApiResult.Unauthorized -> {
                Log.d("ProfileViewModel", result.message ?: Constants.UNAUTHORIZED_ERROR)
            }

            is ApiResult.Error -> {
                Log.d("ProfileViewModel", result.message ?: Constants.UNKNOWN_ERROR)
            }
        }
    }

    private suspend fun cacheProfile(profile: ProfileResponse) {
        val profileDatabase = ProfileDatabase.getDatabase(
            context = tokenManager.context
        )
        val profileDao = profileDatabase.profileDao()
        val profileUseCase = CacheProfileUseCase(CacheProfileRepository(profileDao))
        profileUseCase.cacheProfile(profile)
    }
}