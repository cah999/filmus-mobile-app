package com.example.filmus.ui.screens.main

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.filmus.R
import com.example.filmus.domain.UIState
import com.example.filmus.domain.UserManager
import com.example.filmus.navigation.Screen
import com.example.filmus.ui.screens.main.carousel.Carousel
import com.example.filmus.viewmodel.mainscreen.MainViewModel
import com.example.filmus.viewmodel.mainscreen.MainViewModelFactory
import kotlinx.coroutines.delay
import kotlin.math.min

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavHostController, userManager: UserManager) {
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(userManager))
    val screenState by viewModel.screenState
    val listState = rememberLazyListState()
    val movies = viewModel.movies
    val userReviews = viewModel.userReviews
    val pagerState = rememberPagerState { min(4, movies.size) }
    LaunchedEffect(Unit) {
        val secondScreenResult = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>("newReview")
            ?.value
        Log.d("MainScreen", "LaunchedEffect: $secondScreenResult")
        if ((secondScreenResult != null) && (secondScreenResult == true)) {
            viewModel.updateMovies()
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = listState
    ) {
        item {
            LaunchedEffect(pagerState) {
                while (movies.isNotEmpty()) {
                    delay(5000)
                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
                }
            }

            if (movies.isNotEmpty() && screenState != UIState.LOADING) {
                Carousel(
                    movies = movies.subList(0, min(4, movies.size)),
                    pagerState = pagerState,
                    navController = navController
                )
            } else {
                Box(
                    modifier = Modifier
                        .customShimmer(1000)
                        .width(360.dp)
                        .height(497.dp)
                        .background(Color.Gray)
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
        if (remainingMovies.isEmpty() && screenState != UIState.LOADING) {
            items(3) {
                Row(
                    Modifier.padding(start = 16.dp, bottom = 12.dp, top = 15.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        Modifier
                            .width(95.dp)
                            .height(130.dp)
                            .customShimmer(1000)
                            .background(Color.Gray)
                    )
                    Box(
                        Modifier
                            .width(223.dp)
                            .height(130.dp)
                            .customShimmer(1000)
                            .background(Color.Gray)
                    )
                }
            }
        } else {
            Log.d("MainScreen", "LazyColumn: $remainingMovies")
            items(remainingMovies) { movie ->
                Log.d(
                    "MainScreen",
                    "LazyColumn: ${movie.name} has reviews ${
                        movie.reviews.find {
                            userReviews.contains(it.id)
                        }?.rating
                    }"
                )
                MovieCard(
                    moviePoster = movie.poster,
                    movieName = movie.name,
                    movieYear = movie.year.toString(),
                    movieCountry = movie.country,
                    movieGenres = movie.genres,
                    movieRating = movie.reviews.map { it.rating }.average().toFloat(),
                    userRating = movie.reviews.find { userReviews.contains(it.id) }?.rating,
                    onClick = {
                        navController.navigate("${Screen.Movie.route}/${movie.id}")
                    }
                )
            }
        }
        if (screenState == UIState.REFRESHING) {
            item {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = Color(0xFFFC315E),
                    trackColor = Color(0x1AFC315E)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        if (screenState == UIState.LOADING) {
            items(3) {
                Row(
                    Modifier.padding(start = 16.dp, bottom = 12.dp, top = 15.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        Modifier
                            .width(95.dp)
                            .height(130.dp)
                            .customShimmer(1000)
                            .background(Color.Gray)
                    )
                    Box(
                        Modifier
                            .width(223.dp)
                            .height(130.dp)
                            .customShimmer(1000)
                            .background(Color.Gray)
                    )
                }
            }
        }

    }
    listState.OnBottomReached(onLoadMore = {
        if (screenState != UIState.LOADING)
            viewModel.loadNextPage()
    })
}

@Composable
fun LazyListState.OnBottomReached(
    buffer: Int = 0,
    onLoadMore: () -> Unit
) {
    require(buffer >= 0) { "buffer cannot be negative, but was $buffer" }

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - buffer
        }

    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect { if (it) onLoadMore() }
    }
}
