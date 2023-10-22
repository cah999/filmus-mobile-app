package com.example.filmus.ui.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmus.R
import com.example.filmus.viewmodel.registration.RegistrationViewModel

@Composable
fun GenderSelection(viewModel: RegistrationViewModel) {
    var isMale by viewModel.gender

    Box(
        modifier = Modifier
            .padding(0.dp)
            .width(328.dp)
            .height(42.dp)
            .background(
                color = Color(0x1F767680), shape = RoundedCornerShape(8.dp)
            )
            .alpha(1f),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Button(
                contentPadding = PaddingValues(
                    start = 8.dp,
                    top = 11.dp,
                    end = 8.dp,
                    bottom = 11.dp
                ),
                onClick = {
                    isMale = true
                },
                modifier = Modifier
                    .shadow(
                        elevation = 1.dp,
                        spotColor = Color(0x0A000000),
                        ambientColor = Color(0x0A000000)
                    )
                    .shadow(
                        elevation = 8.dp,
                        spotColor = Color(0x1F000000),
                        ambientColor = Color(0x1F000000)
                    )
                    .border(width = 0.5.dp, color = Color(0x0A000000))
                    .padding(0.5.dp)
                    .width(163.dp)
                    .height(38.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isMale) Color.White else Color(0x1F767680),
                    contentColor = if (isMale) Color(0xFF404040) else Color(0xFF909499)
                ),
                shape = RoundedCornerShape(size = 7.dp)
            ) {
                Text(
                    text = "Мужчина",
                    modifier = Modifier
                        .width(147.dp)
                        .height(16.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(400),
                        color = if (isMale) Color(0xFF404040) else Color(0xFF909499),
                        textAlign = TextAlign.Center,
                    ),
                    textAlign = TextAlign.Center,
                )
            }

            Button(
                contentPadding = PaddingValues(
                    start = 8.dp,
                    top = 11.dp,
                    end = 8.dp,
                    bottom = 11.dp
                ),
                onClick = {
                    isMale = false
                },
                modifier = Modifier
                    .width(163.dp)
                    .height(38.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isMale) Color.White else Color(0x1F767680)
                ),
                shape = RoundedCornerShape(size = 8.dp)
            ) {
                Text(
                    text = "Женщина",
                    modifier = Modifier
                        .width(147.dp)
                        .height(16.dp),

                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(400),
                        color = if (!isMale) Color(0xFF404040) else Color(0xFF909499),
                        textAlign = TextAlign.Center,
                    ),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

}
