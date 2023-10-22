package com.example.filmus.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.filmus.R
import com.example.filmus.navigation.Screen

@Composable
fun WelcomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.welcome_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(35.dp)
        )

        Text(
            text = "Погрузись в мир кино", style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(700),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Мы предлагаем удобный и легкий способ насладиться любимыми фильмами прямо с Вашего мобильного устройства.",
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(400),
                color = Color(0xFFFFFFFF),

                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(35.dp))

        Button(
            onClick = { navController.navigate(Screen.Registration.route) },
            modifier = Modifier
                .width(328.dp)
                .height(42.dp),

            shape = RoundedCornerShape(size = 10.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFC315E),
            ),


        ) {
            Text(
                text = "Регистрация",

                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                )
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = { navController.navigate(Screen.Login.route) },
            modifier = Modifier
                .width(328.dp)
                .height(42.dp),

            shape = RoundedCornerShape(size = 10.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color(0xFF292929)
            ),
        ) {
            Text(
                text = "Войти",

                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFC315E),
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}
