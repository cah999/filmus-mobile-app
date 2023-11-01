package com.example.filmus.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmus.R
import com.example.filmus.ui.marks.ReviewMark

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieCard(
    moviePoster: Int,
    movieName: String,
    movieYear: String,
    movieCountry: String,
    movieGenres: List<String>,
    movieRating: Int?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, bottom = 16.dp)
            .width(328.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = moviePoster),
            contentDescription = null,
            modifier = Modifier
                .width(95.dp)
                .height(130.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = movieName,

                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),
                    ), modifier = Modifier.sizeIn(
                        maxWidth = if (movieRating != null) 175.dp else (328 - 95).dp
                    )
                )

                Spacer(modifier = Modifier.weight(1f))
                if (movieRating != null)
                    ReviewMark(mark = movieRating)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$movieYear · $movieCountry", style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF),
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            val maxGenres = 5
            val visibleGenres = movieGenres.take(maxGenres)
            val showEllipsis = movieGenres.size > maxGenres
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                visibleGenres.forEach { genre ->
                    Text(
                        text = genre, style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFFFFFFFF)
                        ), textAlign = TextAlign.Center, modifier = Modifier
                            .background(
                                color = Color(0xFF404040), shape = RoundedCornerShape(size = 5.dp)
                            )
                            .padding(start = 6.dp, top = 2.dp, end = 6.dp, bottom = 2.dp)
                    )
                }
                if (showEllipsis) {
                    Text(
                        text = "...", style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                        ), modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                    )
                }
            }
        }
    }
}