package com.example.filmus.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class Poster(
    val imageResource: Int, val title: String, val rating: Float? = null
)

@Composable
fun FavoritesScreen(posters: List<Poster>) {
    if (posters.isEmpty()) {
        NoFavoritesPlaceholder()
    } else {
        FavoritesList(posters = posters)
    }
}

@Composable
fun FavoritesList(posters: List<Poster>) {
    val postersInRows = posters.chunked(3) // Разбиваем массив постеров на списки по 3 постера

    LazyColumn {
        items(postersInRows) { postersRow ->
            Row {
                if (postersRow.size >= 2) {
                    // Отображаем первые два постера в одной строке
                    PosterItem(
                        poster = postersRow[0],
                        nextPoster = postersRow[1],
                        fullWidth = false
                    )
                } else if (postersRow.size == 1) {
                    // Если есть только один постер, то отображаем его на всю строку
                    PosterItem(poster = postersRow[0], fullWidth = true)
                }
            }

            if (postersRow.size == 3) {
                // Если в строке есть 3 постера, то отображаем третий постер на всю строку
                PosterItem(poster = postersRow[2], fullWidth = true)
            }
        }
    }
}


@Composable
fun PosterItem(poster: Poster, nextPoster: Poster? = null, fullWidth: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (fullWidth) {
            // Если постер занимает всю строку, то выводим его на всю ширину
            PosterCard(poster = poster, modifier = Modifier.weight(1f))
        } else {
            // Если два постера в одной строке, разделяем строку на две части
            PosterCard(poster = poster, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))
            if (nextPoster != null) {
                PosterCard(poster = nextPoster, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun PosterCard(poster: Poster, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(2f / 3f)
            .height(200.dp), shape = RoundedCornerShape(8.dp)
    ) {
        // Здесь рендерите содержимое постера, например, изображение и название
        // Пример:
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(poster.imageResource),
                contentDescription = poster.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = poster.title,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

