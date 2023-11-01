package com.example.filmus.ui.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmus.R

@Composable
fun CustomBigTextField(
    textFieldValue: String = "",
    placeholder: String = "",
    outlinedColor: Color = Color(0xFF5E5E5E),
    containerColor: Color = Color.Transparent,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = Int.MAX_VALUE,
) {
    Box(
        modifier = Modifier
            .background(color = containerColor, shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = outlinedColor, shape = RoundedCornerShape(3.dp))
            .height(98.dp)
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
    ) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(400),
                color = Color.White
            ),
            keyboardOptions = keyboardOptions,
            visualTransformation = VisualTransformation.None,
            singleLine = false,
            maxLines = maxLines,
            modifier = Modifier.fillMaxSize(),
            cursorBrush = SolidColor(Color.White)
        )
        if (textFieldValue.isEmpty()) {
            Text(
                text = placeholder,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF909499),

                    )
            )
        }
    }
}