package com.example.filmus.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.filmus.ui.screens.main.customShimmer

@Composable
fun LoadingPlaceholder() {
    val placeholderColor = Color.Gray

    LazyColumn(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        item {
            Text(
                text = "Любимое",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(3) { rowIndex ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                when (rowIndex) {
                    0, 2 -> {
                        PosterCardPlaceholder(placeholderColor)
                        PosterCardPlaceholder(placeholderColor)
                    }

                    1 -> {
                        PosterCardPlaceholder(placeholderColor, bigPoster = true)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PosterCardPlaceholder(
    placeholderColor: Color,
    bigPoster: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .width(if (bigPoster) 328.dp else 156.5.dp)
            .height(if (bigPoster) 227.dp else 205.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(if (bigPoster) 328.dp else 205.dp)
                    .fillMaxWidth()
                    .customShimmer(1000)
                    .background(placeholderColor)
            )
        }
    }
}
