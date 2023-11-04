package com.example.filmus.ui.screens.movie

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.NavHostController
import com.example.filmus.R
import com.example.filmus.domain.movie.DetailedMovie
import com.example.filmus.ui.marks.FilmMark
import com.example.filmus.viewmodel.movie.MovieViewModel


// todo blur view
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MovieDetailsScreen(
    movie: DetailedMovie,
    isFavorite: Boolean,
    onFavoriteToggle: (Boolean) -> Unit,
    navController: NavHostController,
    viewModel: MovieViewModel
) {
    val scrollState = rememberScrollState()
    val fadingEdgeModifier = Modifier.fadingEdges(
        scrollState = scrollState
    )
    val maxScrollDistance = with(LocalDensity.current) { (569.5).dp.toPx() }.toInt()
    var showDialog by remember { mutableStateOf(false) }

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
            IconButton(onClick = { navController.popBackStack() }) {
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
                .then(fadingEdgeModifier)
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
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilmMark(movie.reviews.map { it.rating }.average().toFloat())
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

                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                    Text(
                        text = "Подробнее", style = TextStyle(
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
                    modifier = Modifier.fillMaxWidth()
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
                        onClick = { showDialog = true },
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

                            ), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    })

    if (hasScrolledToTitle) {
        Column {
            CenterAlignedTopAppBar(colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color(0xFF1D1D1D)
            ), title = {
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
                IconButton(onClick = { navController.popBackStack() }) {
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
            Divider(
                modifier = Modifier.fillMaxWidth(), color = Color(0xA6545458), thickness = 1.dp
            )
        }
    }
    if (showDialog) {
        ReviewDialog(onDismissRequest = { showDialog = false }, onClick = { showDialog = false })
    }
}


