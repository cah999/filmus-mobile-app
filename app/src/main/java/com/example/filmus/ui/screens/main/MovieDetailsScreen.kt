package com.example.filmus.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmus.R
import java.util.Date

data class Author(
    val userId: String, val nickname: String, val avatar: String
)

data class Review(
    val id: String,
    val rating: Int,
    val reviewText: String,
    val isAnonymous: Boolean,
    val createDateTime: Date,
    val author: Author
)

data class DetailedMovie(
    val name: String,
    val poster: String,
    val year: Int,
    val country: String,
    val genres: List<String>,
    val reviews: List<Review>,
    val time: Int,
    val tagLine: String,
    val description: String,
    val director: String,
    val budget: Int,
    val fees: Int,
    val ageLimit: Int
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MovieDetailsScreen(
    movie: DetailedMovie, isFavorite: Boolean, onFavoriteToggle: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    val maxScrollDistance = with(LocalDensity.current) { (569.5).dp.toPx() }.toInt()

    LaunchedEffect(scrollState.value) {
        if (scrollState.value <= maxScrollDistance / 2) {
            scrollState.animateScrollTo(0)
        } else if (scrollState.value <= maxScrollDistance / 2) {
            scrollState.animateScrollTo(maxScrollDistance)
        }
    }

    val hasScrolledToTitle = scrollState.value >= maxScrollDistance

    Scaffold(containerColor = Color(0xFF1D1D1D), topBar = {
        CenterAlignedTopAppBar(colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFF1D1D1D)
        ), title = {}, navigationIcon = {
            IconButton(onClick = { /* Handle back navigation */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = null,
                )
            }
        }, actions = {})
    }, content = {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(it)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // Замените на ресурс вашего постера
                contentDescription = null,
                modifier = Modifier
                    .width(360.dp)
                    .height(497.dp)
                    .drawWithCache {
                        val gradient = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF1D1D1D)),
                            startY = size.height / 3,
                            endY = size.height
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.Darken)
                        }
                    },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))
            Column(Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    getFilmMark(movie.reviews.map { it.rating }.average().toInt())
                    Text(
                        text = movie.name, style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                        ), modifier = Modifier.width(212.dp)
                    )
                    IconButton(
                        onClick = { onFavoriteToggle(!isFavorite) },
                        modifier = Modifier
                            .background(Color(0xFF404040), shape = CircleShape)
                            .width(40.dp)
                            .height(40.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFavorite) Color(0xFFFC315E) else Color(0xFFFFFFFF),
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                        )
                    }

                }
                Spacer(modifier = Modifier.height(20.dp))

                var isExpanded by remember { mutableStateOf(false) }
                val maxLines = if (isExpanded) Int.MAX_VALUE else 4

                AnimatedVisibility(visible = !isExpanded) {
                    Spacer(modifier = Modifier.height(5.dp))
                }
                Text(
                    text = movie.description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFFCCCCCC),
                    ),
                    maxLines = maxLines,
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawWithCache {
                            val gradient = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    if (isExpanded) Color.Transparent else Color(0xFF1D1D1D)
                                ), startY = size.height / 2, endY = size.height
                            )
                            onDrawWithContent {
                                drawContent()
                                drawRect(gradient, blendMode = BlendMode.Darken)
                            }
                        },
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { isExpanded = !isExpanded }) {
                    Text(
                        text = "Подробнее",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFFFC315E),
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color(0xFFFC315E)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Жанры", style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),

                        )
                )
                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (genre in movie.genres) {
                        Text(
                            text = genre, style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFFFFFFFF),

                                textAlign = TextAlign.Center,
                            ), modifier = Modifier
                                .background(
                                    color = Color(0xFFFC315E),
                                    shape = RoundedCornerShape(size = 5.dp)
                                )
                                .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "О фильме", style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),

                        )
                )

                val infoModifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)


                InfoRow("Год", movie.year.toString(), infoModifier)
                InfoRow("Страна", movie.country, infoModifier)
                InfoRow("Слоган", movie.tagLine, infoModifier)
                InfoRow("Режиссёр", movie.director, infoModifier)
                InfoRow("Бюджет", "\$${movie.budget}", infoModifier)
                InfoRow("Сборы в мире", "\$${movie.fees} ", infoModifier)
                InfoRow("Возраст", "${movie.ageLimit}+", infoModifier)
                InfoRow("Время", "${movie.time} мин.", infoModifier)

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Отзывы", style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFFFFFFF),

                            )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .background(Color(0xFFFC315E), shape = CircleShape)
                            .width(32.dp)
                            .height(32.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.plus),
                            contentDescription = null,
                            tint = Color(0xFFFFFFFF),
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                if (movie.reviews.isNotEmpty()) {
                    movie.reviews.forEach { review ->
                        ReviewCard(review)
                        if (review != movie.reviews.last()) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                } else {
                    Text(
                        text = "Нет отзывов", style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(50),
                            color = Color(0xFFFFFFFF),

                            ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    })

    if (hasScrolledToTitle) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color(0xFF1D1D1D)
            ),
            title = {
                Text(
                    text = movie.name,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),

                        ),
                    textAlign = TextAlign.Center,
                )
            }, navigationIcon = {
                IconButton(onClick = { /* Handle back navigation */ }) {
                    Icon(
                        painterResource(id = R.drawable.back), contentDescription = null
                    )
                }
            }, actions = {
                Box(Modifier.padding(end = 15.dp), contentAlignment = Alignment.CenterEnd) {
                    IconButton(
                        onClick = { onFavoriteToggle(!isFavorite) },
                        modifier = Modifier
                            .background(Color(0xFF404040), shape = CircleShape)
                            .width(40.dp)
                            .height(40.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFavorite) Color(0xFFFC315E) else Color(0xFFFFFFFF),
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                        )
                    }
                }
            })
    }
}

@Composable
fun InfoRow(title: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(400),
                color = Color(0xFF909499),

                ),
            modifier = Modifier.width(100.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value, style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(400),
                color = Color(0xFFFFFFFF),

                )
        )
    }
}

@Composable
fun ReviewCard(review: Review) {
    val isUser = true // todo check if user review
    Column {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = if (review.isAnonymous) painterResource(id = R.drawable.anonymous) else painterResource(
                    id = R.drawable.ic_launcher_background
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column() {
                Text(
                    text = if (review.isAnonymous) "Анонимный пользователь" else review.author.nickname,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),

                        ),
                    textAlign = TextAlign.Center
                )
                if (isUser) Text(
                    text = "мой отзыв", style = TextStyle(
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF909499),

                        )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            val rating = review.rating
            val img = getFilmMarkImage(rating)
            if (img != null) {
                Image(
                    painter = painterResource(id = img),
                    contentDescription = null,
                    modifier = Modifier
                        .width(38.dp)
                        .height(26.dp),
                    contentScale = ContentScale.Crop
                )
            }
            if (isUser) {
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .background(Color(0xFF404040), shape = CircleShape)
                        .width(26.dp)
                        .height(26.dp)

                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                            .graphicsLayer { rotationZ = 90f }
                    )
                }
            }
        }
        Text(
            text = review.reviewText,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(400),
                color = Color(0xFFFFFFFF),

                ),
            modifier = Modifier.padding(top = 10.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        val dateFormat = java.text.SimpleDateFormat("dd.MM.yyyy")
        Text(
            text = dateFormat.format(review.createDateTime),
            style = TextStyle(fontSize = 12.sp, color = Color(0xFF8C8C8C))
        )
    }
}

fun getFilmMarkImage(mark: Int): Int? {
    return when (mark) {
        2 -> R.drawable.mark_2
        3 -> R.drawable.mark_3
        4 -> R.drawable.mark_4
        6 -> R.drawable.mark_6
        8 -> R.drawable.mark_8
        9 -> R.drawable.mark_9
        else -> null
    }
}

@Composable
fun getFilmMark(mark: Int) {
    val (fontColor, markColor) = remember {
        when (mark) {
            2 -> Pair(0xFFFFFFFF, 0xFFE64646)
            3 -> Pair(0xFFFFFFFF, 0xFFF05C44)
            4 -> Pair(0xFFFFFFFF, 0xFFFFA000)
            6 -> Pair(0xFF1D1D1D, 0xFFFFD54F)
            8 -> Pair(0xFF1D1D1D, 0xFFA3CD4A)
            9 -> Pair(0xFFFFFFFF, 0xFF4BB34B)
            else -> Pair(0xFFE64646, 0xFFFFFFFF)
        }
    }

    val text = mark.toFloat().toString()

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
