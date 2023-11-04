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
import com.example.filmus.domain.favorite.Poster

@Composable
fun PosterItem(poster: Poster, nextPoster: Poster? = null, fullWidth: Boolean) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (fullWidth) {
            PosterCard(
                poster = poster, 227.dp, 328.dp

            )
        } else {
            PosterCard(
                poster = poster, 244.dp, 156.5.dp
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (nextPoster != null) {
                PosterCard(
                    poster = nextPoster, 244.dp, 156.5.dp
                )
            }
        }
    }
}