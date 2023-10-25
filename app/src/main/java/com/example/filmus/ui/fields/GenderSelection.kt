package com.example.filmus.ui.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmus.R

@Composable
fun GenderSelection(defaultIsMale: Boolean, onGenderSelected: (Boolean) -> Unit) {
    var isMale by remember { mutableStateOf(defaultIsMale) }

    Row(
        modifier = Modifier
            .padding(0.dp)
            .width(328.dp)
            .height(42.dp)
            .background(
                color = Color(0x1F767680), shape = RoundedCornerShape(8.dp)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                isMale = true
                onGenderSelected(true)
            },
            modifier = Modifier
                .width(163.dp)
                .height(38.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isMale) Color.White else Color.Transparent,
                contentColor = if (isMale) Color(0xFF404040) else Color(0xFF909499)
            ),
            shape = RoundedCornerShape(size = 7.dp)
        ) {
            Text(
                "Мужчина",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = if (isMale) Color(0xFF404040) else Color(0xFF909499),
                    textAlign = TextAlign.Center,
                ),
            )
        }

        Button(
            onClick = {
                isMale = false
                onGenderSelected(false)
            },
            modifier = Modifier
                .width(163.dp)
                .height(38.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isMale) Color.White else Color.Transparent,
            ),
            shape = RoundedCornerShape(size = 8.dp)
        ) {
            Text(
                text = "Женщина",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = if (!isMale) Color(0xFF404040) else Color(0xFF909499),
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}


@Preview
@Composable
fun GenderBtn() {
    Button(
        onClick = {
        },
        modifier = Modifier
            .width(163.dp)
            .height(38.dp)
            .padding(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF404040)
        ),
        shape = RoundedCornerShape(size = 7.dp)
    ) {
        Text(
            text = "Мужчина",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(400),
                color = Color(0xFF404040),
                textAlign = TextAlign.Center,
            ),
        )
    }
}