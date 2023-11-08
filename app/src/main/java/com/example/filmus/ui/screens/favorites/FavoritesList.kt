package com.example.filmus.ui.screens.favorites

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.filmus.R
import com.example.filmus.domain.main.Movie
import com.example.filmus.viewmodel.favorites.FavoritesViewModel

@Composable
fun FavoritesList(
    movies: List<Movie>,
    userReviews: List<String>,
    navController: NavController,
    viewModel: FavoritesViewModel
) {
    val postersInRows = movies.chunked(3)

    LazyColumn(Modifier.padding(top = 16.dp)) {
        item {
            Text(
                text = "Любимое",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(postersInRows) { postersRow ->
            Row {
                if (postersRow.size >= 2) {
                    PosterItem(
                        movie = postersRow[0],
                        nextMovie = postersRow[1],
                        fullWidth = false,
                        userReviews = userReviews,
                        navController = navController,
                        viewModel = viewModel
                    )
                } else if (postersRow.size == 1) {
                    PosterItem(
                        movie = postersRow[0],
                        fullWidth = true,
                        userReviews = userReviews,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }

            if (postersRow.size == 3) {
                PosterItem(
                    movie = postersRow[2],
                    fullWidth = true,
                    userReviews = userReviews,
                    navController = navController,
                    viewModel = viewModel
                )
            }
            if (postersRow == postersInRows.last()) {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
