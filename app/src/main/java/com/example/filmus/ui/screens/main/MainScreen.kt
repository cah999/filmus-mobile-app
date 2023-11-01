package com.example.filmus.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.filmus.R
import com.example.filmus.domain.main.Movie
import com.example.filmus.navigation.Screen
import com.example.filmus.ui.screens.main.carousel.Carousel
import com.example.filmus.viewmodel.mainscreen.MainViewModel
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay
import kotlin.math.min

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel, movies: List<Movie>) {
    val pagerState = rememberPagerState { min(4, movies.size) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            LaunchedEffect(pagerState) {
                while (movies.isNotEmpty()) {
                    delay(5000)
                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
                }
            }

            if (movies.isNotEmpty()) {
                Carousel(
                    movies = movies.subList(0, min(4, movies.size)),
                    pagerState = pagerState,
                    navController = navController
                )
            } else {
                Box(
                    modifier = Modifier
                        .shimmer()
                        .width(360.dp)
                        .height(497.dp)
                        .background(Color(0xFF292929))
                )
            }
        }

        item {
            Text(
                text = "Каталог", style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(700),
                    color = Color.White,
                ), modifier = Modifier.padding(start = 16.dp, bottom = 15.dp, top = 16.dp)
            )
        }

        val remainingMovies = movies.subList(min(4, movies.size), movies.size)
        if (remainingMovies.isEmpty()) {
            item {
                Text(
                    text = "Пока здесь нет фильмов :(",
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(700),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                )
            }
        } else {
            items(remainingMovies) { movie ->
                MovieCard(
                    moviePoster = movie.poster,
                    movieName = movie.name,
                    movieYear = movie.year.toString(),
                    movieCountry = movie.country,
                    movieGenres = movie.genres,
                    movieRating = movie.reviews.map { it.rating }.average().toInt(),
                    onClick = {
                        navController.navigate("${Screen.Movie.route}/${movie.id}")
                    }
                )
            }
        }
    }
}


