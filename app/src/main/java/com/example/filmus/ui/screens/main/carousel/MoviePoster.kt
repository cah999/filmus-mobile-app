package com.example.filmus.ui.screens.main.carousel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.filmus.domain.main.Movie

@Composable
fun MoviePoster(movie: Movie, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(360.dp)
            .height(497.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = movie.poster),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}