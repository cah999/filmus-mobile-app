package com.example.filmus.ui.screens.login

import android.content.Context
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.filmus.R
import com.example.filmus.common.Constants
import com.example.filmus.domain.UIState
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.repository.TokenManager
import com.example.filmus.ui.fields.CustomTextField
import com.example.filmus.ui.navigation.Screen
import com.example.filmus.viewmodel.login.LoginViewModel
import com.example.filmus.viewmodel.login.LoginViewModelFactory

@Composable
fun LoginScreen(
    navController: NavHostController,
    tokenManager: TokenManager
) {
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(tokenManager)
    )
    val vibratorManager =
        LocalContext.current.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    val vibrator = vibratorManager.defaultVibrator
    var username by viewModel.username
    var password by viewModel.password
    var state by viewModel.state
    var errorMessage by viewModel.errorMessage

    val containerColor by animateColorAsState(
        targetValue = if (state == UIState.ERROR) Color(0x1AE64646) else Color.Transparent,
        label = ""
    )

    val outlinedColor by animateColorAsState(
        targetValue = if (state == UIState.ERROR) Color(0xFFE64646) else Color(0xFF909499),
        label = ""
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
    ) {
        Text(
            text = "Вход",

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
            text = "Логин",

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
                username = it
            },
            textFieldValue = username,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            outlinedColor = outlinedColor,
            containerColor = containerColor,
            isPassword = false,
            vibrator = vibrator
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Пароль", style = TextStyle(
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
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            outlinedColor = outlinedColor,
            containerColor = containerColor,
            isPassword = true,
            vibrator = vibrator
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
            Spacer(modifier = Modifier.height(15.dp))
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
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        Constants.VIBRATION_BUTTON_CLICK,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
                state = UIState.LOADING
                viewModel.login(username, password) { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            navController.navigate(Screen.Main.route)
                            {
                                popUpTo(0)
                            }
                        }

                        is ApiResult.Error -> {
                            state = UIState.ERROR
                            errorMessage = result.message.toString()

                        }

                        else -> {
                            state = UIState.ERROR
                            errorMessage = Constants.UNKNOWN_ERROR
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .alpha(if (username.isNotEmpty() && password.isNotEmpty()) 1f else 0.45f),
            shape = RoundedCornerShape(size = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFC315E),
                disabledContainerColor = Color(0xFFFC315E),
            ),
            enabled = username.isNotEmpty() && password.isNotEmpty(),
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
                text = "Еще нет аккаунта? ", style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFC4C8CC),
                    textAlign = TextAlign.Center,
                )
            )
            Text(
                text = "Зарегистрируйтесь",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFC315E),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.clickable {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            Constants.VIBRATION_BUTTON_CLICK,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                    navController.navigate(Screen.Registration.route)
                },
            )
        }

    }
}



