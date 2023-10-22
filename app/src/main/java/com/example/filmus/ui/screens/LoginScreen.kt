package com.example.filmus.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.filmus.domain.login.LoginResult
import com.example.filmus.navigation.Screen
import com.example.filmus.ui.fields.CustomTextField
import com.example.filmus.viewmodel.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController, viewModel: LoginViewModel
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
                onValueChange = { username = it },
                textFieldValue = username,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                isPassword = false
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
                onValueChange = { password = it },
                textFieldValue = password,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.login(username, password) { result ->
                        when (result) {
                            is LoginResult.Success -> {
                                navController.navigate(Screen.Welcome.route)
                            }

                            is LoginResult.Error -> {
                                // Handle error
                            }
                        }
                    }
                },
                modifier = Modifier
                    .width(328.dp)
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
                        navController.navigate(Screen.Registration.route)
                    },
                )
            }
        }
    })
}


