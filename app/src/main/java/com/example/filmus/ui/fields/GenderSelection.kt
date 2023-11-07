package com.example.filmus.ui.fields

import android.util.Log
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
import androidx.compose.runtime.MutableState
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
fun GenderSelection(defaultIsMale: MutableState<Int>, onGenderSelected: (Int) -> Unit) {
    Log.d("GenderSelection", "defaultIsMale: ${defaultIsMale.value}")
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
                defaultIsMale.value = 1
                onGenderSelected(1)
            },
            modifier = Modifier
                .width(163.dp)
                .height(38.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (defaultIsMale.value == 1) Color.White else Color.Transparent,
                contentColor = if (defaultIsMale.value == 1) Color(0xFF404040) else Color(0xFF909499)
            ),
            shape = RoundedCornerShape(size = 7.dp)
        ) {
            Text(
                "Мужчина",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = if (defaultIsMale.value == 1) Color(0xFF404040) else Color(0xFF909499),
                    textAlign = TextAlign.Center,
                ),
            )
        }

        Button(
            onClick = {
                defaultIsMale.value = 0
                onGenderSelected(0)
            },
            modifier = Modifier
                .width(163.dp)
                .height(38.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (defaultIsMale.value == 0) Color.White else Color.Transparent,
            ),
            shape = RoundedCornerShape(size = 8.dp)
        ) {
            Text(
                text = "Женщина",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = if (defaultIsMale.value == 0) Color(0xFF404040) else Color(0xFF909499),
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}
