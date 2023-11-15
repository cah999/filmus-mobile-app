package com.example.filmus.ui.screens.favorites.poster

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.filmus.domain.main.Movie

@Composable
fun PosterItem(
    movie: Movie,
    nextMovie: Movie? = null,
    fullWidth: Boolean,
    userReviews: List<String>,
    onCardClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if (fullWidth) {
            PosterCard(
                movie = movie,
                modifier = Modifier
                    .fillMaxWidth(),
                userReviews,
                onCardClick,
                onDeleteClick,
            )
        } else {
            PosterCard(
                movie = movie,
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                userReviews,
                onCardClick,
                onDeleteClick,
            )
            if (nextMovie != null) {
                PosterCard(
                    movie = nextMovie,
                    modifier = Modifier
                        .fillMaxWidth(),
                    userReviews,
                    onCardClick,
                    onDeleteClick,
                )
            }
        }
    }
}