package com.example.filmus.ui.screens

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.filmus.R
import com.example.filmus.domain.login.UIState
import com.example.filmus.domain.registration.RegistrationResult
import com.example.filmus.navigation.Screen
import com.example.filmus.ui.fields.CustomTextField
import com.example.filmus.viewmodel.registration.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPwdScreen(
    navController: NavController,
    viewModel: RegistrationViewModel
) {
    var password by viewModel.password
    var confirmPassword by viewModel.passwordRepeat
    val buttonEnabled =
        password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
    var state by remember { mutableStateOf(UIState.DEFAULT) }
    var errorMessage by remember { mutableStateOf("") }

    val outlinedColor by animateColorAsState(
        targetValue = if (state == UIState.ERROR) Color(0xFFE64646) else Color(0xFF909499),
        label = ""
    )
    val containerColor by animateColorAsState(
        targetValue = if (state == UIState.ERROR) Color(0x1AE64646) else Color.Transparent,
        label = ""
    )

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Image(
                painter = painterResource(id = R.drawable.bar_logo), contentDescription = null
            )
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back), contentDescription = null
                )
            }
        })
    }, content = { it ->
        Log.d("LoginScreen", "LoginScreen: $it")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
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
                text = "Пароль",

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
                    if (state == UIState.ERROR) state = UIState.DEFAULT
                    password = it
                },
                textFieldValue = password,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                outlinedColor = outlinedColor,
                containerColor = containerColor,
                isPassword = true
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Повторите пароль", style = TextStyle(
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
                    if (state == UIState.ERROR) state = UIState.DEFAULT
                    confirmPassword = it
                },
                textFieldValue = confirmPassword,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                outlinedColor = outlinedColor,
                containerColor = containerColor,
                isPassword = true
            )
            if (state == UIState.ERROR) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage, style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFFE64646),
                    ), modifier = Modifier.alpha(1f)
                )
            } else if (state == UIState.LOADING) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = Color(0xFFFC315E),
                    trackColor = Color(0x1AFC315E)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    state = UIState.LOADING
                    val passwordValidation = viewModel.validatePassword()
                    if (passwordValidation.isValid) {
                        state = UIState.LOADING
                        viewModel.register { result ->
                            when (result) {
                                is RegistrationResult.Success -> {
                                    Log.d("RegistrationPwdScreen", "Registration success")
                                    navController.navigate(Screen.Welcome.route)
                                }

                                is RegistrationResult.Error -> {
                                    Log.d("RegistrationPwdScreen", "Registration error")
                                    Log.d("RegistrationPwdScreen", "$result")
                                }
                            }
                        }
                    } else {
                        errorMessage = passwordValidation.errorMessage
                        state = UIState.ERROR
                    }
                },
                modifier = Modifier
                    .width(328.dp)
                    .height(42.dp)
                    .alpha(if (buttonEnabled) 1f else 0.45f),
                contentPadding = PaddingValues(
                    start = 15.dp, top = 12.dp, end = 15.dp, bottom = 12.dp
                ),
                shape = RoundedCornerShape(size = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFC315E),
                    disabledContainerColor = Color(0xFFFC315E),
                ),
                enabled = buttonEnabled,
            ) {
                Text(
                    text = "Войти",

                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    )
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
    })
}
