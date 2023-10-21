package com.example.filmus.ui.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmus.R

@Composable
fun CustomTextField(
    // todo вынести пустую строку в константы
//    textFieldValue: String = SyncStateContract.Constants.EMPTY_STRING,
    textFieldValue: String = "",
    outlinedColor: Color = Color.Gray,
    containerColor: Color = Color.Transparent,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    BasicTextField(
        modifier = Modifier
            .background(
                color = containerColor,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = outlinedColor,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth(),
        value = textFieldValue,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = 15.sp,
            fontFamily = FontFamily(Font(R.font.inter)),
            fontWeight = FontWeight(400),
            color = Color(0xFFFFFFFF),
        ),
        keyboardOptions = keyboardOptions,
        singleLine = true,
        enabled = true,
        cursorBrush = SolidColor(Color.White),
        decorationBox = @Composable { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                innerTextField()
            }
        }
    )
}