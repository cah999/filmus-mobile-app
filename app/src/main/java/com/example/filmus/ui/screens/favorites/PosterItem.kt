package com.example.filmus.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.filmus.domain.main.Movie
import com.example.filmus.viewmodel.favorites.FavoritesViewModel

@Composable
fun PosterItem(
    movie: Movie,
    nextMovie: Movie? = null,
    fullWidth: Boolean,
    userReviews: List<String>,
    navController: NavController,
    viewModel: FavoritesViewModel
) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (fullWidth) {
            PosterCard(
                movie = movie, 227.dp, 328.dp, userReviews, navController, viewModel
            )
        } else {
            PosterCard(
                movie = movie, 244.dp, 156.5.dp, userReviews, navController, viewModel
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (nextMovie != null) {
                PosterCard(
                    movie = nextMovie, 244.dp, 156.5.dp, userReviews, navController, viewModel
                )
            }
        }
    }
}