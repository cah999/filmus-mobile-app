package com.example.filmus.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.filmus.R
import com.example.filmus.navigation.Screen
import com.example.filmus.ui.fields.CustomDateField
import com.example.filmus.ui.fields.CustomTextField
import com.example.filmus.ui.fields.GenderSelection
import com.example.filmus.viewmodel.registration.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavController, viewModel: RegistrationViewModel
) {
    var name by viewModel.name
    var login by viewModel.login
    var email by viewModel.email
    var birthDate by viewModel.birthDate
    val buttonEnabled =
        name.isNotEmpty() && login.isNotEmpty() && email.isNotEmpty() && birthDate.isNotEmpty()


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
    },
        containerColor = Color(0xFF1D1D1D),
        content = { it ->
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
                    onValueChange = { name = it },
                    textFieldValue = name,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    isPassword = false
                )
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
                GenderSelection(viewModel)
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
                    onValueChange = { login = it },
                    textFieldValue = login,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    isPassword = false
                )

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
                    onValueChange = { email = it },
                    textFieldValue = email,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    isPassword = false
                )

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
                    onValueChange = { birthDate = it },
                    textFieldValue = birthDate,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                )
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = {
                        navController.navigate(Screen.RegistrationPwd.route)
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
        })
}


@Preview
@Composable
fun Btn() {
    Button(
        onClick = {
        },
        modifier = Modifier
            .width(328.dp)
            .height(42.dp),
        shape = RoundedCornerShape(size = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFC315E),
            disabledContainerColor = Color(0xFFFC315E),
        ),

        ) {
        Text(
            text = "Продолжить",

            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(600),
                color = Color(0xFFFFFFFF),
            ),
        )
    }
}
