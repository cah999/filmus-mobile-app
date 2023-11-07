package com.example.filmus.ui.screens.registration

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.filmus.R
import com.example.filmus.navigation.Screen
import com.example.filmus.ui.fields.CustomDateField
import com.example.filmus.ui.fields.CustomTextField
import com.example.filmus.ui.fields.GenderSelection
import com.example.filmus.viewmodel.registration.RegistrationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavHostController, viewModel: RegistrationViewModel
) {
    // todo FIX REGISTRATION \2023-11-06 02:10:13.486 28734-28734 RegistrationPwdScreen   com.example.filmus                   D  Error(message=Registration failed, errors={"message":"User Registration Failed","errors":{"DuplicateUserName":{"rawValue":null,"attemptedValue":null,"errors":[{"exception":null,"errorMessage":"Username 'qwerty' is already taken."}],"validationState":1,"isContainerNode":false,"children":null}}})
    var name by viewModel.name
    var login by viewModel.login
    var email by viewModel.email
    var birthDate by viewModel.birthDate
    var gender by viewModel.gender
    val buttonEnabled =
        name.isNotEmpty() && login.isNotEmpty() && email.isNotEmpty() && birthDate.isNotEmpty()

    val nameValidationState = viewModel.validationStates.value.getOrNull(0)
    val loginValidationState = viewModel.validationStates.value.getOrNull(1)
    val emailValidationState = viewModel.validationStates.value.getOrNull(2)
    val birthDateValidationState = viewModel.validationStates.value.getOrNull(3)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
    ) {
        Text(
            text = "Регистрация",

            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(700),
                color = Color(0xFFFFFFFF),
            ), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Имя",

            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))


        CustomTextField(
            onValueChange = {
                if (nameValidationState != null && !nameValidationState.isValid) {
                    nameValidationState.isValid = true
                }
                name = it
            },
            textFieldValue = name,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            containerColor = Color(viewModel.getContainerColor(nameValidationState)),
            outlinedColor = Color(viewModel.getOutlineColor(nameValidationState)),
            isPassword = false
        )

        if (nameValidationState != null && !nameValidationState.isValid) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = nameValidationState.errorMessage, style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFE64646),
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Пол",

            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        GenderSelection(
            defaultIsMale = viewModel.gender,
            onGenderSelected = { gender = it })
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Логин", style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            onValueChange = {
                if (loginValidationState != null && !loginValidationState.isValid) {
                    loginValidationState.isValid = true
                }
                login = it
            },
            textFieldValue = login,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            containerColor = Color(viewModel.getContainerColor(loginValidationState)),
            outlinedColor = Color(viewModel.getOutlineColor(loginValidationState)),
            isPassword = false
        )

        if (loginValidationState != null && !loginValidationState.isValid) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = loginValidationState.errorMessage, style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFE64646),
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Электронная почта", style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            onValueChange = {
                if (emailValidationState != null && !emailValidationState.isValid) {
                    emailValidationState.isValid = true
                }
                email = it
            },
            textFieldValue = email,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            containerColor = Color(viewModel.getContainerColor(emailValidationState)),
            outlinedColor = Color(viewModel.getOutlineColor(emailValidationState)),
            isPassword = false
        )

        if (emailValidationState != null && !emailValidationState.isValid) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = emailValidationState.errorMessage, style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFE64646),
                )
            )
        }


        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Дата рождения", style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomDateField(
            onValueChange = {
                if (birthDateValidationState != null && !birthDateValidationState.isValid) {
                    birthDateValidationState.isValid = true
                }
                birthDate = it
                Log.d("RegistrationScreen", "RegistrationScreen: $viewModel.birthDate")
            },
            textFieldValue = birthDate,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            containerColor = Color(viewModel.getContainerColor(birthDateValidationState)),
            outlinedColor = Color(viewModel.getOutlineColor(birthDateValidationState)),
        )
        if (birthDateValidationState != null && !birthDateValidationState.isValid) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = birthDateValidationState.errorMessage, style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFE64646),
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                viewModel.validateRegistrationData()
                val validationStateList = viewModel.validationStates.value

                val isDataValid = validationStateList.all { it.isValid }


                if (isDataValid) {
                    navController.navigate(Screen.RegistrationPwd.route)
                }
            },
            modifier = Modifier
                .width(328.dp)
                .height(42.dp)
                .alpha(
                    if (buttonEnabled) 1f else 0.45f
                ),
            shape = RoundedCornerShape(size = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFC315E),
                disabledContainerColor = Color(0xFFFC315E),
            ),
            enabled = buttonEnabled,

            ) {
            Text(
                text = "Продолжить",

                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                ),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Уже есть аккаунт? ", style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFC4C8CC),
                    textAlign = TextAlign.Center,
                )
            )
            Text(
                text = "Войдите",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFC315E),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route)
                },
            )
        }

    }
}
