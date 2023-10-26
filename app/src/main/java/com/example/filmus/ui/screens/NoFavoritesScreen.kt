package com.example.filmus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmus.R

@Composable
fun NoFavoritesPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Любимое",
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(700),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "У вас пока нет любимых фильмов",
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(700),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Выберите понравившийся фильм в каталоге и добавьте его в любимые",
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(400),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )
    }
}
