package com.example.filmus.ui.marks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
fun FilmMark(mark: Float) {
    val (fontColor, markColor) = remember {
        when {
            (mark >= 0f && mark < 3f) -> Pair(0xFFFFFFFF, 0xFFE64646)
            (mark >= 3f && mark < 4f) -> Pair(0xFFFFFFFF, 0xFFF05C44)
            (mark >= 4f && mark < 6f) -> Pair(0xFFFFFFFF, 0xFFFFA000)
            (mark >= 6f && mark < 8f) -> Pair(0xFF1D1D1D, 0xFFFFD54F)
            (mark >= 8f && mark < 9f) -> Pair(0xFF1D1D1D, 0xFFA3CD4A)
            (mark >= 9f) -> Pair(0xFFFFFFFF, 0xFF4BB34B)
            else -> Pair(0xFFE64646, 0xFFFFFFFF)
        }
    }

    val text = mark.toString()

    Box(
        modifier = Modifier
            .width(51.dp)
            .height(26.dp)
            .background(color = Color(markColor), shape = RoundedCornerShape(5.dp)),
    ) {
        Text(
            text = text, style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(500),
                color = Color(fontColor),
            ), textAlign = TextAlign.Center, modifier = Modifier
                .fillMaxSize()
                .padding(top = 2.dp)
        )
    }

}