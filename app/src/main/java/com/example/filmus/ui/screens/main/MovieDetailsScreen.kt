package com.example.filmus.ui.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmus.R
import java.util.Date

data class Author(
    val userId: String,
    val nickname: String,
    val avatar: String
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
    movie: DetailedMovie,
    isFavorite: Boolean,
    onFavoriteToggle: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    val maxScrollDistance = with(LocalDensity.current) { 562.dp.toPx() }.toInt()

//    val coroutineScope = rememberCoroutineScope()

//    LaunchedEffect(scrollState.value) {
//        if (scrollState.value <= maxScrollDistance) {
//            coroutineScope.launch {
//                scrollState.animateScrollTo(maxScrollDistance)
//            }
//        }
//    }

    val hasScrolledToTitle = scrollState.value >= maxScrollDistance

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF1D1D1D)
                ),
                title = { Text(text = "Movie Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onFavoriteToggle(!isFavorite) },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = {
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
                        .height(497.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = movie.reviews.map { it.rating }.average().toString(),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = movie.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                    Icon(
                        imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                var isExpanded by remember { mutableStateOf(false) }

                if (isExpanded) {
                    Text(
                        text = movie.description,
                        style = TextStyle(fontSize = 16.sp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                } else {
                    Text(
                        text = movie.description.take(100), // Показываем только первые 100 символов
                        style = TextStyle(fontSize = 16.sp)
                    )

                    Row(
                        Modifier.clickable { isExpanded = true },
                    ) {
                        Row {
                            Text(text = "Подробнее")
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Жанры:",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (genre in movie.genres) {
                        Text(
                            text = genre,
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFFFFFFFF),

                                textAlign = TextAlign.Center,
                            ),
                            modifier = Modifier
                                .width(73.dp)
                                .height(28.dp)
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
                    text = "О фильме:",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
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

                Text(
                    text = "Отзывы:",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (movie.reviews.isNotEmpty()) {
                    movie.reviews.forEach { review ->
                        ReviewCard(review)
                    }
                } else {
                    Text(text = "Нет отзывов", fontSize = 14.sp, textAlign = TextAlign.Center)
                }
            }
        }
    )

    if (hasScrolledToTitle) {
        // Кастомный TopAppBar для скролла
        TopAppBar(
            title = { Text(text = movie.name) },
            navigationIcon = {
                IconButton(onClick = { /* Handle back navigation */ }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { onFavoriteToggle(!isFavorite) },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null
                    )
                }
            }
        )
    }
}

@Composable
fun InfoRow(title: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            style = TextStyle(fontSize = 14.sp)
        )
    }
}

@Composable
fun ReviewCard(review: Review) {
    val isUser = false // todo check if user review
    Column {
        Row(Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xFFEBEDF0),
                        shape = RoundedCornerShape(size = 100.dp)
                    ),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(Modifier.height(40.dp)) {
                Text(
                    text = review.author.nickname,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
                )
                if (isUser)
                    Text(
                        text = "Ваш отзыв",
                        style = TextStyle(fontSize = 12.sp)
                    )
            }
            Spacer(modifier = Modifier.weight(1f))
            val rating = review.rating
            val img = when (rating) {
                2 -> R.drawable.mark_2
                3 -> R.drawable.mark_3
                4 -> R.drawable.mark_4
                6 -> R.drawable.mark_6
                8 -> R.drawable.mark_8
                9 -> R.drawable.mark_9
                else -> null
            }
            Log.d("ReviewCard", "rating: $rating, img: $img")
            if (img != null) {
                Image(
                    painter = painterResource(id = img),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Crop
                )
            }
            if (isUser)
                Spacer(modifier = Modifier.width(10.dp))
            IconButton(onClick = { /* Handle edit review */ }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                )
            }
        }
        Text(
            text = review.reviewText,
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.padding(top = 10.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = review.createDateTime.toString(),
            style = TextStyle(fontSize = 12.sp, color = Color(0xFF8C8C8C))
        )
    }
}


@Preview
@Composable
fun Details() {
    val author = Author(
        userId = "user123",
        nickname = "MovieBuff",
        avatar = "https://avatar-url.com/avatar.png"
    )

    val review = Review(
        id = "review456",
        rating = 6,
        reviewText = "A fantastic movie! Loved it!",
        isAnonymous = false,
        createDateTime = Date(),
        author = author
    )

    val movie = DetailedMovie(
        name = "Inception",
        poster = "https://poster-url.com/inception.png",
        year = 2010,
        country = "USA",
        genres = listOf("Action", "Adventure", "Sci-Fi"),
        reviews = listOf(review, review, review, review, review, review),
        time = 148,
        tagLine = "The dream is real",
        description = "Inception is a 2010 science fiction action film some long long long long long long long long long lnoglnognglnglnlgnolgafsaljkfhjblsafkjasfkLGJGKLFKGLDJSGFKHJLASDGKJGHKJSDAHKJLGSDAHKJLGSDALK;HJGSHLDJA;GLH;SADGLSDLAGHJL",
        director = "Christopher Nolan",
        budget = 160_000_000,
        fees = 828_322_032,
        ageLimit = 13
    )
    MovieDetailsScreen(movie = movie, isFavorite = false, onFavoriteToggle = { })
}