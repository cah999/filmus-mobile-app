package com.example.filmus.viewmodel.profile

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.common.Constants
import com.example.filmus.domain.TokenManager
import com.example.filmus.domain.UIState
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.createApiService
import com.example.filmus.domain.database.profile.ProfileDatabase
import com.example.filmus.domain.database.profile.ProfileEntity
import com.example.filmus.domain.profile.CacheProfileUseCase
import com.example.filmus.domain.profile.ProfileResponse
import com.example.filmus.domain.profile.ProfileUseCase
import com.example.filmus.repository.profile.ApiProfileRepository
import com.example.filmus.repository.profile.CacheProfileRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileViewModel(private val tokenManager: TokenManager) : ViewModel() {
    var id = ""
    var nickname = mutableStateOf("")
    var email = mutableStateOf("")
    var avatarLink = mutableStateOf("")
    var name = mutableStateOf("")
    var gender = mutableIntStateOf(0)
    var birthDate = mutableStateOf("")
    val isLogout = mutableStateOf(false)
    val logoutMessage = mutableStateOf("")
    val screenState = mutableStateOf(UIState.DEFAULT)

    fun logout() {
        viewModelScope.launch {
            val apiService = createApiService(token = tokenManager.getToken())
            val profileRepository = ApiProfileRepository(apiService)
            val profileUseCase = ProfileUseCase(profileRepository)
            when (profileUseCase.logout()) {
                is ApiResult.Success -> {
                    tokenManager.clearToken()
                    isLogout.value = true
                    logoutMessage.value = Constants.LOGOUT_MESSAGE
                }

                is ApiResult.Error -> {
                    isLogout.value = true
                    logoutMessage.value = Constants.UNKNOWN_ERROR
                }

                is ApiResult.Unauthorized -> {
                    isLogout.value = true
                    logoutMessage.value = Constants.LOGOUT_MESSAGE
                }
            }
        }
    }

    fun getInfo(force: Boolean = false) {
        if (force) {
            screenState.value = UIState.LOADING
            viewModelScope.launch {
                fetchAndPopulate()
                screenState.value = UIState.DEFAULT
            }
            return
        }
        viewModelScope.launch {
            val profileDatabase = ProfileDatabase.getDatabase(
                context = tokenManager.context
            )
            val profileDao = profileDatabase.profileDao()
            val profileUseCase = CacheProfileUseCase(CacheProfileRepository(profileDao))
            profileUseCase.getProfile().collect { cachedProfile ->
                if (cachedProfile != null) {
                    populateUI(cachedProfile)
                } else {
                    fetchAndPopulate()
                }
            }
        }
    }

    private suspend fun fetchAndPopulate() {
        val token = tokenManager.getToken()
        if (token != null) {
            val apiService = createApiService(token)
            val profileRepository = ApiProfileRepository(apiService)
            val profileUseCase = ProfileUseCase(profileRepository)

            when (val result = profileUseCase.getInfo()) {
                is ApiResult.Success -> {
                    if (result.data != null) {
                        cacheProfile(result.data)
                        populateUI(result.data)
                    }
                }

                is ApiResult.Unauthorized -> {
                    screenState.value = UIState.UNAUTHORIZED
                }

                is ApiResult.Error -> {
                    screenState.value = UIState.ERROR
                }
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

    private fun populateUI(profile: ProfileEntity) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        id = profile.id
        nickname.value = profile.nickname
        email.value = profile.email
        avatarLink.value = profile.avatarLink ?: ""
        name.value = profile.name
        gender.intValue = profile.gender
        birthDate.value = outputFormat.format(inputFormat.parse(profile.birthDate) ?: "")
    }

    private fun populateUI(profile: ProfileResponse) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        id = profile.id
        nickname.value = profile.nickname
        email.value = profile.email
        avatarLink.value = profile.avatarLink ?: ""
        name.value = profile.name
        gender.intValue = profile.gender
        birthDate.value = outputFormat.format(inputFormat.parse(profile.birthDate) ?: "")
    }

    fun updateInfo() {
        viewModelScope.launch {
            val apiService = createApiService(tokenManager.getToken())
            val profileRepository = ApiProfileRepository(apiService)
            val profileUseCase = ProfileUseCase(profileRepository)

            val inputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = outputFormat.format(inputFormat.parse(birthDate.value) ?: "")
            val profileResponse = ProfileResponse(
                id = id,
                nickname = nickname.value,
                email = email.value,
                avatarLink = if (avatarLink.value == "") null else avatarLink.value,
                name = name.value,
                birthDate = date,
                gender = gender.intValue
            )
            when (profileUseCase.updateInfo(profileResponse)) {
                is ApiResult.Success -> {
                    cacheProfile(profileResponse)
                    populateUI(profileResponse)
                }

                is ApiResult.Unauthorized -> {
                    screenState.value = UIState.UNAUTHORIZED
                }

                is ApiResult.Error -> {
                    screenState.value = UIState.UNAUTHORIZED
                }
            }
        }
    }
}