package com.example.filmus.ui.screens.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.filmus.R
import com.example.filmus.navigation.AppNavigator
import com.example.filmus.navigation.BottomBar

data class Poster(
    val imageResource: Int, val title: String, val rating: Int? = null
)

@Composable
fun FavoritesScreen(
    posters: List<Poster>, navController: NavController, appNavigator: AppNavigator
) {
    Scaffold(bottomBar = { BottomBar(navController = navController, appNavigator = appNavigator) },
        content = {
            if (posters.isEmpty()) {
                NoFavoritesPlaceholder(it)
            } else {
                FavoritesList(posters = posters, it)
            }
        })

}

@Composable
fun FavoritesList(posters: List<Poster>, it: PaddingValues) {
    val postersInRows = posters.chunked(3)

    LazyColumn(Modifier.padding(it)) {
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
        }
    }
}


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

@Composable
fun PosterCard(poster: Poster, height: Dp, width: Dp) {
    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.width(width),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(height)
                    .width(width)
            ) {
                Image(
                    painter = painterResource(poster.imageResource),
                    contentDescription = poster.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                val ratingImageResource = when (poster.rating) {
                    2 -> R.drawable.mark_2
                    3 -> R.drawable.mark_3
                    4 -> R.drawable.mark_4
                    6 -> R.drawable.mark_6
                    8 -> R.drawable.mark_8
                    9 -> R.drawable.mark_9
                    else -> null
                }
                if (ratingImageResource != null) {
                    Image(
                        painter = painterResource(ratingImageResource),
                        contentDescription = "Rating",
                        modifier = Modifier
                            .padding(2.dp)
                            .width(38.dp)
                            .height(26.dp)
                            .align(Alignment.TopEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = poster.title, style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                )
            )
        }
    }
}



