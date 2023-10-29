package com.example.filmus.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.filmus.R
import com.example.filmus.viewmodel.mainscreen.MovieViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: MovieViewModel, movies: List<Movie>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Carousel(movies = movies)

        Text(
            text = "Каталог",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
            modifier = Modifier.padding(top = 15.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Карточки фильмов
        MovieCard(
            moviePoster = R.drawable.ic_launcher_background,
            movieName = "Название фильма",
            movieYear = "Год фильма",
            movieCountry = "Страна фильма",
            movieGenres = listOf("Жанр 1", "Жанр 2"),
            movieRating = 8.5f
        )

        Spacer(modifier = Modifier.height(10.dp))

    }
}

data class Review(
    val id: String,
    val rating: Int
)

data class Movie(
    val name: String,
    val poster: Int,
    val year: Int,
    val country: String,
    val genres: List<String>,
    val reviews: List<Review>
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(
    movies: List<Movie>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        movies.size
    }

    Box(
        modifier = modifier
    ) {
        HorizontalPager(state = pagerState) { page ->
            MoviePoster(movie = movies[page])
        }

        // todo BLUR NOT WORKING?!?!?!?
        DotsIndicator(
            pagerState = pagerState,
            itemCount = movies.size,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        )
    }
}

@Composable
fun MoviePoster(movie: Movie) {
    Box(
        modifier = Modifier
            .width(360.dp)
            .height(497.dp)
    ) {
        Image(
            painter = painterResource(id = movie.poster),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotsIndicator(
    pagerState: PagerState,
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    val displayItemCount = if (itemCount > 4) 4 else itemCount

    Box(
        modifier = modifier
            .width(72.dp)
            .height(24.dp)
            .background(Color(0x1AFFFFFF).copy(alpha = 0.1f), RoundedCornerShape(28.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            repeat(displayItemCount) { i ->
                val isSelected = pagerState.currentPage == i
                val color = if (isSelected) Color.White else Color.Transparent
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color = color, shape = CircleShape)
                        .border(1.dp, Color.White, shape = CircleShape)
                )
            }
        }
    }
}


//
//@Composable
//fun MovieList(viewModel: MovieViewModel) {
//    val pagingItems = viewModel.pagingItems.collectAsLazyPagingItems()
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        items(pagingItems) { movie ->
//            movie?.let { MovieCard(movie = it) }
//        }
//
//        pagingItems.apply {
//            when {
//                loadState.append is LoadState.Loading -> {
//                    item {
//                        CircularProgressIndicator(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                        )
//                    }
//                }
//
//                loadState.append is LoadState.Error -> {
//                    val errorState = pagingItems.loadState.append as LoadState.Error
//                    item {
//                        Text(
//                            text = "Error: ${errorState.error.localizedMessage}",
//                            color = Color.Red,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}


@Composable
fun MovieCard(
    moviePoster: Int,
    movieName: String,
    movieYear: String,
    movieCountry: String,
    movieGenres: List<String>,
    movieRating: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Постер фильма слева
            Image(
                painter = painterResource(id = moviePoster),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Основная информация фильма (название, год, страна)
            Column {
                Text(text = movieName, style = TextStyle(fontWeight = FontWeight.Bold))
                Text(text = movieYear)
                Text(text = movieCountry)

                Spacer(modifier = Modifier.height(10.dp))

                // Жанры фильма
                Text(
                    text = movieGenres.joinToString(separator = ", "),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Оценка фильма
            Text(
                text = movieRating.toString(),
                modifier = Modifier
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.small)
            )
        }
    }
}

