package com.example.filmus.viewmodel.registration

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.common.Constants
import com.example.filmus.domain.UIState
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.domain.api.createApiService
import com.example.filmus.domain.database.profile.ProfileDatabase
import com.example.filmus.domain.profile.CacheProfileUseCase
import com.example.filmus.domain.profile.ProfileResponse
import com.example.filmus.domain.profile.ProfileUseCase
import com.example.filmus.domain.registration.register.RegistrationRequest
import com.example.filmus.domain.registration.register.RegistrationResult
import com.example.filmus.domain.registration.register.RegistrationUseCase
import com.example.filmus.domain.registration.validation.FieldValidationState
import com.example.filmus.domain.registration.validation.ValidateRegistrationDataUseCase
import com.example.filmus.domain.registration.validation.ValidationData
import com.example.filmus.repository.TokenManager
import com.example.filmus.repository.profile.ApiProfileRepository
import com.example.filmus.repository.profile.CacheProfileRepository
import com.example.filmus.repository.registration.RegistrationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RegistrationViewModel(
    private val tokenManager: TokenManager
) :
    ViewModel() {


    var name = mutableStateOf(Constants.EMPTY)
    var gender = mutableIntStateOf(1)
    var login = mutableStateOf(Constants.EMPTY)
    var email = mutableStateOf(Constants.EMPTY)
    var birthDate = mutableStateOf(Constants.EMPTY)
    var password = mutableStateOf(Constants.EMPTY)
    var passwordRepeat = mutableStateOf(Constants.EMPTY)
    val validationStates = mutableStateOf(emptyList<FieldValidationState>())
    var screenState = mutableStateOf(UIState.DEFAULT)
    private val validateUseCase = ValidateRegistrationDataUseCase()
    fun getOutlineColor(state: FieldValidationState?): Int {
        return if (state?.isValid == false) {
            0xFFE64646.toInt()
        } else {
            0xFF909499.toInt()
        }
    }

    fun getContainerColor(state: FieldValidationState?): Int {
        return if (state?.isValid == false) {
            0x1AE64646
        } else {
            0x00000000
        }
    }

    fun validatePassword(): FieldValidationState {
        return validateUseCase.validatePassword(
            password.value, passwordRepeat.value
        )
    }

    fun validateRegistrationData() {
        val data = ValidationData(
            name = name.value, login = login.value, email = email.value, birthDate = birthDate.value
        )

        val states = validateUseCase.validate(data)
        validationStates.value = states
    }

    private fun formatDateToISO8601(birthDate: String): String {
        val inputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

        val date = inputFormat.parse(birthDate)

        return outputFormat.format(date ?: Date())
    }

    fun register(onResult: (RegistrationResult) -> Unit) {
        viewModelScope.launch {
            val apiService = createApiService()
            val registrationRepository = RegistrationRepository(apiService)
            val registrationUseCase = RegistrationUseCase(registrationRepository)
            val result = registrationUseCase.register(
                RegistrationRequest(
                    name = name.value,
                    login = login.value,
                    password = password.value,
                    email = email.value,
                    birthDate = formatDateToISO8601(birthDate.value),
                    gender = gender.intValue
                )
            )
            if (result is RegistrationResult.Success) {
                getProfile()
                tokenManager.saveToken(result.token)
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
                Log.d("ProfileViewModel", Constants.UNAUTHORIZED_ERROR)
            }

            is ApiResult.Error -> {
                Log.d("ProfileViewModel", Constants.UNKNOWN_ERROR)
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