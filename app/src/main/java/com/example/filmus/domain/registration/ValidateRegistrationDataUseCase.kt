package com.example.filmus.domain.registration

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ValidateRegistrationDataUseCase {
    fun validate(data: RegistrationData): List<FieldValidationState> {
        val validationStates = mutableListOf<FieldValidationState>()

        validationStates.add(validateName(data.name))
        validationStates.add(validateLogin(data.login))
        validationStates.add(validateEmail(data.email))
        validationStates.add(validateBirthDate(data.birthDate))


        return validationStates
    }


    private fun validateName(name: String): FieldValidationState {
        if (name.isEmpty()) {
            return FieldValidationState(false, "Имя не может быть пустым")
        }
        if (name.length < 2) {
            return FieldValidationState(false, "Имя должно содержать как минимум 2 символа")
        }

        return FieldValidationState(true, "")
    }

    private fun validateLogin(login: String): FieldValidationState {
        if (login.isEmpty()) {
            return FieldValidationState(false, "Логин не может быть пустым")
        }
        if (login.length < 5) {
            return FieldValidationState(false, "Логин должен содержать как минимум 5 символов")
        }

        return FieldValidationState(true, "")
    }

    private fun validateEmail(email: String): FieldValidationState {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (email.isEmpty()) {
            return FieldValidationState(false, "Электронная почта не может быть пустой")
        }
        if (!email.matches(emailPattern.toRegex())) {
            return FieldValidationState(false, "Неправильный формат электронной почты")
        }

        return FieldValidationState(true, "")
    }

    private fun validateBirthDate(birthDate: String): FieldValidationState {
        if (birthDate.isEmpty()) {
            return FieldValidationState(false, "Дата рождения не может быть пустой")
        }

        val dateFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        val maxDate = dateFormat.parse("1900-01-01")

        val selectedDate = dateFormat.parse(birthDate)

        if (selectedDate.after(currentDate)) {
            return FieldValidationState(false, "Дата рождения не может быть в будущем")
        }

        if (selectedDate.before(maxDate)) {
            return FieldValidationState(false, "Дата рождения должна быть после 1900 года")
        }

        return FieldValidationState(true, "")
    }

    fun validatePassword(password: String, confirmPassword: String): FieldValidationState {
        if (password.isEmpty()) {
            return FieldValidationState(false, "Пароль не может быть пустым")
        }
        if (password.length < 6) {
            return FieldValidationState(false, "Пароль должен содержать минимум 6 символов")
        }
        if (password != confirmPassword) {
            return FieldValidationState(false, "Пароли не совпадают")
        }
        return FieldValidationState(true, "")
    }

}
