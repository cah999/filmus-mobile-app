package com.example.filmus.ui.screens.favorites

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.filmus.domain.favorite.Poster

@Composable
fun FavoritesList(posters: List<Poster>) {
    val postersInRows = posters.chunked(3)

    LazyColumn {
        items(postersInRows) { postersRow ->
            Row {
                if (postersRow.size >= 2) {
                    PosterItem(
                        poster = postersRow[0], nextPoster = postersRow[1], fullWidth = false
                    )
                } else if (postersRow.size == 1) {
                    PosterItem(poster = postersRow[0], fullWidth = true)
                }
            }

            if (postersRow.size == 3) {
                PosterItem(poster = postersRow[2], fullWidth = true)
            }
            if (postersRow == postersInRows.last()) {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
