package com.example.filmus.domain.registration.validation

import com.example.filmus.common.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ValidateRegistrationDataUseCase {
    companion object {
        const val NAME_EMPTY_ERROR = "Имя не может быть пустым"
        const val NAME_LENGTH_ERROR = "Имя должно содержать как минимум 2 символа"
        const val LOGIN_EMPTY_ERROR = "Логин не может быть пустым"
        const val LOGIN_LENGTH_ERROR = "Логин должен содержать как минимум 5 символов"
        const val LOGIN_FORMAT_ERROR = "Логин должен содержать только английские буквы и цифры"
        const val EMAIL_EMPTY_ERROR = "Электронная почта не может быть пустой"
        const val EMAIL_FORMAT_ERROR = "Неправильный формат электронной почты"
        const val BIRTH_DATE_EMPTY_ERROR = "Дата рождения не может быть пустой"
        const val BIRTH_DATE_FORMAT_ERROR = "Неправильный формат даты рождения"
        const val BIRTH_DATE_RANGE_ERROR = "Неправильная дата рождения"
        const val PASSWORD_EMPTY_ERROR = "Пароль не может быть пустым"
        const val PASSWORD_LENGTH_ERROR = "Пароль должен содержать минимум 6 символов"
        const val PASSWORD_MATCH_ERROR = "Пароли не совпадают"
    }

    fun validate(data: ValidationData): List<FieldValidationState> {
        val validationStates = mutableListOf<FieldValidationState>()

        validationStates.add(validateName(data.name))
        if (data.login != null) validationStates.add(validateLogin(data.login))
        validationStates.add(validateEmail(data.email))
        validationStates.add(validateBirthDate(data.birthDate))

        return validationStates
    }

    private fun validateName(name: String): FieldValidationState {
        if (name.isEmpty()) {
            return FieldValidationState(false, NAME_EMPTY_ERROR)
        }
        if (name.length < 2) {
            return FieldValidationState(false, NAME_LENGTH_ERROR)
        }

        return FieldValidationState(true, Constants.EMPTY)
    }

    private fun validateLogin(login: String): FieldValidationState {
        if (login.isEmpty()) {
            return FieldValidationState(false, LOGIN_EMPTY_ERROR)
        }
        if (login.length < 5) {
            return FieldValidationState(false, LOGIN_LENGTH_ERROR)
        }

        val pattern = "^[a-zA-Z0-9]*$".toRegex()

        if (!pattern.matches(login)) {
            return FieldValidationState(false, LOGIN_FORMAT_ERROR)
        }

        return FieldValidationState(true, Constants.EMPTY)
    }

    private fun validateEmail(email: String): FieldValidationState {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (email.isEmpty()) {
            return FieldValidationState(false, EMAIL_EMPTY_ERROR)
        }
        if (!email.matches(emailPattern.toRegex())) {
            return FieldValidationState(false, EMAIL_FORMAT_ERROR)
        }

        return FieldValidationState(true, Constants.EMPTY)
    }

    private fun validateBirthDate(birthDate: String): FieldValidationState {
        if (birthDate.isEmpty()) {
            return FieldValidationState(false, BIRTH_DATE_EMPTY_ERROR)
        }

        val dateFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        val userDate = dateFormat.parse(birthDate)
        val minDate = dateFormat.parse("01011900")
        val maxDate = dateFormat.parse(dateFormat.format(Calendar.getInstance().time))
        val datePattern = """^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])\d{4}$""".toRegex()
        if (!birthDate.matches(datePattern)) {
            return FieldValidationState(false, BIRTH_DATE_FORMAT_ERROR)
        }

        if (userDate != null) {
            if (userDate < minDate || userDate > maxDate) {
                return FieldValidationState(false, BIRTH_DATE_RANGE_ERROR)
            }
        }

        return FieldValidationState(true, Constants.EMPTY)
    }

    fun validatePassword(password: String, confirmPassword: String): FieldValidationState {
        if (password.isEmpty()) {
            return FieldValidationState(false, PASSWORD_EMPTY_ERROR)
        }
        if (password.length < 6) {
            return FieldValidationState(false, PASSWORD_LENGTH_ERROR)
        }
        if (password != confirmPassword) {
            return FieldValidationState(false, PASSWORD_MATCH_ERROR)
        }
        return FieldValidationState(true, Constants.EMPTY)
    }
}