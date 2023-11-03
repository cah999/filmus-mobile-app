package com.example.filmus.ui.screens.main.carousel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.filmus.domain.main.Movie

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(
    movies: List<Movie>, pagerState: PagerState, navController: NavHostController
) {
    Box(
        modifier = Modifier
            .width(360.dp)
            .height(497.dp)
    ) {
        HorizontalPager(state = pagerState) { page ->
            MoviePoster(movie = movies[page],
                onClick = { navController.navigate("movie/${movies[page].id}") })
        }

        DotsIndicator(
            pagerState = pagerState,
            itemCount = movies.size,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
        )
    }
}
