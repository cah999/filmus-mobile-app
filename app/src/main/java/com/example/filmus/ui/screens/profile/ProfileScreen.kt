package com.example.filmus.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.filmus.ui.fields.CustomDateField
import com.example.filmus.ui.fields.CustomTextField
import com.example.filmus.ui.fields.GenderSelection

@Composable
fun ProfileScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(true) }
    var birthDate by remember { mutableStateOf("") }
    var buttonEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_background),
                contentDescription = null,
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(Color.Black, shape = CircleShape),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        }



        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Ivan ivan ivan",

            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(700),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            ), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {

            },
            modifier = Modifier
                .width(328.dp)
                .height(42.dp),
            shape = RoundedCornerShape(size = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
            ),

            ) {
            Text(
                text = "Выйти из аккаунта",

                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFC315E),

                    textAlign = TextAlign.Center,
                )
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Электронная почта",

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
                if (!buttonEnabled) buttonEnabled = true
                email = it
            },
            textFieldValue = email,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            isPassword = false
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Ссылка на аватар", style = TextStyle(
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
                if (!buttonEnabled) buttonEnabled = true
                link = it
            },
            textFieldValue = link,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            isPassword = false
        )

        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Имя", style = TextStyle(
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
                if (!buttonEnabled) buttonEnabled = true
                name = it
            },
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
        GenderSelection(defaultIsMale = gender, onGenderSelected = { selectedGender ->
            gender = selectedGender
            if (!buttonEnabled) buttonEnabled = true
        })
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
                if (!buttonEnabled) buttonEnabled = true
                birthDate = it
            },
            textFieldValue = birthDate,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {

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
                text = "Сохранить",

                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                ),
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {

            },
            modifier = Modifier
                .width(328.dp)
                .height(42.dp),
            shape = RoundedCornerShape(size = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF292929),
                disabledContainerColor = Color(0xFF292929),
            ),

            ) {
            Text(
                text = "Отмена",

                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFC315E),
                    textAlign = TextAlign.Center,
                )
            )
        }
        // spacer with alpha 1
        Spacer(modifier = Modifier.height(15.dp))

    }
}