package com.example.filmus.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.navigation.NavController
import com.example.filmus.R
import com.example.filmus.viewmodel.mainscreen.MovieViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: MovieViewModel, movies: List<Movie>) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Carousel(movies = movies)
        Text(
            text = "Каталог", style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(700),
                color = Color(0xFFFFFFFF),
            ),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 15.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp),
            content = {
                items(movies) { movie ->
                    MovieCard(
                        moviePoster = movie.poster,
                        movieName = movie.name,
                        movieYear = movie.year.toString(),
                        movieCountry = movie.country,
                        movieGenres = movie.genres,
                        movieRating = movie.reviews.map { it.rating }.average().toInt()
                    )
                }
            })
    }
}

data class Review(
    val id: String, val rating: Int
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
    movies: List<Movie>, modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = 0, initialPageOffsetFraction = 0f
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
    pagerState: PagerState, itemCount: Int, modifier: Modifier = Modifier
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


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieCard(
    moviePoster: Int,
    movieName: String,
    movieYear: String,
    movieCountry: String,
    movieGenres: List<String>,
    movieRating: Int?
) {

    Row(
        modifier = Modifier
            .width(328.dp)
            .padding(bottom = 16.dp)
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
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = movieName,

                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),
                    ),
                    modifier = Modifier.sizeIn(
                        maxWidth = if (movieRating != null) 175.dp else (328-95).dp
                    )
                )
                val img = when (movieRating) {
                    2 -> R.drawable.mark_2
                    3 -> R.drawable.mark_3
                    4 -> R.drawable.mark_4
                    6 -> R.drawable.mark_6
                    8 -> R.drawable.mark_8
                    9 -> R.drawable.mark_9
                    else -> null
                }
                Spacer(modifier = Modifier.weight(1f))
                if (img != null) {
                    Image(
                        painter = painterResource(id = img),
                        contentDescription = null,
                        alignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .width(45.dp)
                            .height(26.dp)
                    )
                }
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