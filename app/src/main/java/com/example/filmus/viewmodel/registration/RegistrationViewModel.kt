package com.example.filmus.viewmodel.registration

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmus.api.RegistrationRequest
import com.example.filmus.api.createApiService
import com.example.filmus.domain.UIState
import com.example.filmus.domain.UserManager
import com.example.filmus.domain.registration.register.RegistrationData
import com.example.filmus.domain.registration.register.RegistrationResult
import com.example.filmus.domain.registration.register.RegistrationUseCase
import com.example.filmus.domain.registration.validation.FieldValidationState
import com.example.filmus.domain.registration.validation.ValidateRegistrationDataUseCase
import com.example.filmus.repository.registration.RegistrationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RegistrationViewModel(
    private val userManager: UserManager
) :
    ViewModel() {


    var name = mutableStateOf("")
    var gender = mutableIntStateOf(1)
    var login = mutableStateOf("")
    var email = mutableStateOf("")
    var birthDate = mutableStateOf("")
    var password = mutableStateOf("")
    var passwordRepeat = mutableStateOf("")
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
        val data = RegistrationData(
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
        viewModelScope.launch() {
            Log.d("RegistrationViewModel", "register: birthDate: ${birthDate}")
            Log.d("RegistrationViewModel", "register: email: ${email}")
            val apiService = createApiService()
            val registrationRepository = RegistrationRepository(apiService, userManager)
            val registrationUseCase = RegistrationUseCase(registrationRepository)
            val result = registrationUseCase.register(
                RegistrationRequest(
                    name = name.value,
                    login = login.value,
                    password = password.value,
                    email = email.value,
                    birthDate = formatDateToISO8601(birthDate.value),
                    gender = gender.value
                )
            )
            if (result is RegistrationResult.Success) {
                userManager.checkToken()
            }
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
}