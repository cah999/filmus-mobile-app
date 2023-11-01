package com.example.filmus.ui.screens.main


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.filmus.R
import com.example.filmus.ui.fields.CustomBigTextField
import com.gowtham.ratingbar.RatingBar

@Composable
fun ReviewDialog(
    onDismissRequest: () -> Unit,
    onClick: () -> Unit
) {
    var rating by remember { mutableIntStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }
    val buttonEnabled = rating != 0 && reviewText.isNotEmpty()

    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Surface(
            shape = RoundedCornerShape(5.dp), color = Color(0xFF1D1D1D)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Оставить отзыв",
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),

                        ),
                )
                Spacer(modifier = Modifier.height(15.dp))
                RatingBar(
                    value = rating.toFloat(),
                    painterEmpty = painterResource(id = R.drawable.rating),
                    painterFilled = painterResource(id = R.drawable.rating_filled),
                    numOfStars = 10,
                    size = 25.dp,
                    spaceBetween = (2.5).dp,
                    onValueChange = {
                        rating = it.toInt()
                    },
                    onRatingChanged = {
                        Log.d("TAG", "onRatingChanged: $it")
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                CustomBigTextField(
                    placeholder = "Напишите отзыв",
                    textFieldValue = reviewText,
                    onValueChange = { reviewText = it }
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .width(16.dp)
                            .height(16.dp)

                    ) {
                        Checkbox(
                            checked = isAnonymous,
                            onCheckedChange = { isAnonymous = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFFC315E),
                                uncheckedColor = Color(0xFFE1E3E6),
                                checkmarkColor = Color(0xFFFFFFFF)
                            ),
                            modifier = Modifier.fillMaxSize() // Заполняет размером Box
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Анонимный отзыв",

                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFFFFFFFF),
                        )
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .width(308.dp)
                        .height(42.dp)
                        .alpha(
                            if (buttonEnabled) 1f else 0.45f
                        ),
                    shape = RoundedCornerShape(size = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFC315E),
                        disabledContainerColor = Color(0xFFFC315E),
                    ),
                    enabled = buttonEnabled,

                    ) {
                    Text(
                        text = "Сохранить",

                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .width(308.dp)
                        .height(42.dp),
                    shape = RoundedCornerShape(size = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF292929),
                        disabledContainerColor = Color(0xFF292929),
                    ),

                    ) {
                    Text(
                        text = "Отмена",

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

    }
}