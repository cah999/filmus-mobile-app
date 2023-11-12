package com.example.filmus.ui.screens.favorites.poster

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.filmus.R
import com.example.filmus.domain.main.Movie
import com.example.filmus.ui.marks.ReviewMark

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PosterCard(
    movie: Movie,
    modifier: Modifier,
    userReviews: List<String>,
    onCardClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    val imagePainter = rememberAsyncImagePainter(model = movie.poster)
    val movieRating = movie.reviews.find { userReviews.contains(it.id) }?.rating
    val showDialog = remember { mutableStateOf(false) }
    val mContext = LocalContext.current
    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.combinedClickable(
            onClick = { onCardClick(movie.id) },
            onLongClick = { showDialog.value = true }),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Column {
            Box {
                Image(
                    painter = imagePainter,
                    contentDescription = movie.name,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .clip(RoundedCornerShape(3.dp))
                )
                if (movieRating != null) {
                    ReviewMark(
                        mark = movieRating, modifier = Modifier
                            .padding(10.dp)
                            .align(
                                Alignment.TopEnd
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = movie.name, style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                )
            )
        }
    }
    if (showDialog.value) {
        AlertDialog(onDismissRequest = { showDialog.value = false }, title = {
            Text(
                text = "Удалить фильм из избранного?", style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFFFFFF),
                )
            )
        }, text = {
            Text(
                text = "Вы уверены, что хотите удалить фильм из избранного?", style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                )
            )
        }, confirmButton = {
            ClickableText(
                AnnotatedString("Удалить"), onClick = {
                    onDeleteClick(movie.id)
                    showDialog.value = false
                    Toast.makeText(
                        mContext, "Фильм удален из избранного", Toast.LENGTH_SHORT
                    ).show()
                }, style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFC315E),
                ), modifier = Modifier.padding(10.dp)
            )
        }, dismissButton = {
            ClickableText(
                AnnotatedString("Отмена"), onClick = {
                    showDialog.value = false
                }, style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF909499),
                ), modifier = Modifier.padding(10.dp)
            )
        }, containerColor = Color(0xFF1F1F1F), shape = RoundedCornerShape(10.dp)
        )
    }
}
