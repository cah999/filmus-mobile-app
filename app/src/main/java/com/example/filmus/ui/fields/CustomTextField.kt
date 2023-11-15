package com.example.filmus.ui.fields

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmus.R
import com.example.filmus.common.Constants

@Composable
fun CustomTextField(
    textFieldValue: String = Constants.EMPTY,
    outlinedColor: Color = Color.Gray,
    containerColor: Color = Color.Transparent,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isPassword: Boolean,
    isPasswordVisible: MutableState<Boolean> = remember { mutableStateOf(!isPassword) },
    vibrator: Vibrator

) {
    BasicTextField(modifier = Modifier
        .background(
            color = containerColor, shape = RoundedCornerShape(8.dp)
        )
        .border(
            width = 1.dp, color = outlinedColor, shape = RoundedCornerShape(8.dp)
        )
        .fillMaxWidth(),
        value = textFieldValue,
        onValueChange = {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    Constants.VIBRATION_TYPING,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
            onValueChange(it)
        },
        textStyle = TextStyle(
            fontSize = 15.sp,
            fontFamily = FontFamily(Font(R.font.inter)),
            fontWeight = FontWeight(400),
            color = Color(0xFFFFFFFF),
        ),
        keyboardOptions = keyboardOptions,
        visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
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
                if (isPassword) {
                    IconButton(
                        onClick = { isPasswordVisible.value = !isPasswordVisible.value },
                        modifier = Modifier
                            .padding(1.dp)
                            .width(20.dp)
                            .height(20.dp)
                    ) {
                        Image(
                            painter = if (isPasswordVisible.value) painterResource(id = R.drawable.eye_crossed) else painterResource(
                                id = R.drawable.eye
                            ),
                            contentDescription = if (isPasswordVisible.value) "Hide Password" else "Show Password"
                        )
                    }
                }
            }
        })
}
