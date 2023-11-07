package com.example.filmus.viewmodel.profile

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.ProfileResponse
import com.example.filmus.api.ProfileResult
import com.example.filmus.api.ProfileUpdateResult
import com.example.filmus.api.createApiService
import com.example.filmus.api.toProfileEntity
import com.example.filmus.domain.UserManager
import com.example.filmus.domain.database.ProfileEntity
import com.example.filmus.domain.profile.ProfileUseCase
import com.example.filmus.repository.profile.ProfileRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileViewModel(private val userManager: UserManager) : ViewModel() {
    var id = ""
    var nickname = mutableStateOf("")
    var email = mutableStateOf("")
    var avatarLink = mutableStateOf("")
    var name = mutableStateOf("")
    var gender = mutableIntStateOf(0)
    var birthDate = mutableStateOf("")
    val isLogout = mutableStateOf(false)
    private val logoutMessage = mutableStateOf("")

    fun logout() {
        viewModelScope.launch() {
            val apiService = createApiService(token = userManager.getToken())
            val profileRepository = ProfileRepository(apiService, userManager)
            val profileUseCase = ProfileUseCase(profileRepository)
            userManager.clearCache()
            profileUseCase.logout()
            isLogout.value = true
            logoutMessage.value = "Вы успешно вышли из аккаунта!"
        }
    }

    fun getInfo() {
        Log.d("ProfileViewModel", "getInfo: called")
        viewModelScope.launch {
            userManager.getProfile().collect { cachedProfile ->
                if (cachedProfile != null) {
                    Log.d("ProfileViewModel", "getInfo: cachedProfile: $cachedProfile")
                    populateUI(cachedProfile)
                } else {
                    fetchAndPopulate()
                }
            }
        }
    }

    private suspend fun fetchAndPopulate(): Boolean {
        val token = userManager.getToken()
        if (token != null) {
            val apiService = createApiService(token)
            val profileRepository = ProfileRepository(apiService, userManager)
            val profileUseCase = ProfileUseCase(profileRepository)

            when (val result = profileUseCase.getInfo()) {
                is ProfileResult.Success -> {
                    userManager.cacheProfile(result.profile)
                    populateUI(result.profile.toProfileEntity())
                    return true
                }

                is ProfileResult.Unauthorized -> {
                    logout()
                    isLogout.value = true
                    logoutMessage.value =
                        "Ваша сессия истекла, пожалуйста, войдите в аккаунт заново!"
                }

                else -> {}
            }
        }
        return false
    }

    private fun populateUI(profile: ProfileEntity) {
        Log.d("ProfileViewModel", "populateUI: profile: $profile")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        id = profile.id
        nickname.value = profile.nickname
        email.value = profile.email
        avatarLink.value = profile.avatarLink
        name.value = profile.name
        gender.value = profile.gender
        birthDate.value = outputFormat.format(inputFormat.parse(profile.birthDate) ?: "")
    }

    fun updateInfo() {
        viewModelScope.launch {
            val apiService = createApiService(userManager.getToken())
            val profileRepository = ProfileRepository(apiService, userManager)
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
                gender = gender.value
            )
            val res = profileUseCase.updateInfo(profileResponse)
            if (res is ProfileUpdateResult.Success) {
                userManager.cacheProfile(profileResponse)
                populateUI(profileResponse.toProfileEntity())
            } else if (res is ProfileUpdateResult.Unauthorized) {
                logout()
                isLogout.value = true
                logoutMessage.value =
                    "Ваша сессия истекла, пожалуйста, войдите в аккаунт заново!"
            }
        }
    }
}