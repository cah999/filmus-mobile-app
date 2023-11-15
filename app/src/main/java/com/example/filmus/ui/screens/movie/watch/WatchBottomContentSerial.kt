package com.example.filmus.ui.screens.movie.watch

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmus.R
import com.example.filmus.domain.movieAPI.Episode
import com.example.filmus.domain.movieAPI.ExMovie
import com.example.filmus.domain.movieAPI.Season
import com.example.filmus.domain.movieAPI.Stream
import com.example.filmus.domain.movieAPI.Translation
import kotlinx.coroutines.launch

@Composable
fun WatchBottomSheetContentSerial(
    movie: ExMovie,
    selectedTranslation: Translation,
    onTranslationSelected: (Translation) -> Unit,
    selectedSeason: Season?,
    onSeasonSelected: (Season) -> Unit,
    selectedEpisode: Episode?,
    onEpisodeSelected: (Episode) -> Unit,
    onWatchClicked: () -> Unit,
    selectedResolution: Stream?,
    onResolutionSelected: (Stream) -> Unit,
    onLoadResolutions: suspend (Int, Translation, Int, Int) -> List<Stream>,
    onLoadSeasonsForTranslation: suspend (Int, Int) -> List<Season>
) {
    var showDialog by remember { mutableStateOf<String?>(null) }
    var seasons by remember { mutableStateOf(emptyList<Season>()) }
    var resolutions by remember { mutableStateOf(emptyList<Stream>()) }

    val seasonLocale = "Сезон"
    val episodeLocale = "Эпизод"

    val scope = rememberCoroutineScope()

    val context = LocalContext.current


    // Function to load resolutions for the current translation, season, and episode
    suspend fun loadResolutions() {
        // Implement the API call to load resolutions for the selected translation, season, and episode
        // For example:
        scope.launch {
            try {
                resolutions = onLoadResolutions(
                    movie.id, selectedTranslation, selectedSeason?.id ?: 1, selectedEpisode?.id ?: 1
                )
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }

            // Once the resolutions are loaded, update the selectedResolution state with the first resolution (if any)
            if (resolutions.isNotEmpty()) {
                onResolutionSelected(resolutions.first())
            }
        }
    }

    // Function to load seasons for the selected translation
    LaunchedEffect(selectedTranslation) {
        selectedSeason?.let {
            onSeasonSelected(it)
        }
        scope.launch {
            try {
                seasons = onLoadSeasonsForTranslation(movie.id, selectedTranslation.id)
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to load episodes for the selected season
    LaunchedEffect(selectedSeason, selectedTranslation) {
        selectedEpisode?.let {
            onEpisodeSelected(it)
        }
    }

    // Function to load resolutions for the selected translation, season, and episode
    LaunchedEffect(selectedSeason, selectedEpisode, selectedTranslation) {
        loadResolutions()
    }

    Column(
        modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Translation dropdown
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDialog = "translation"
            }) {
            OutlinedTextField(
                value = selectedTranslation.name,
                onValueChange = { /* Implement if needed */ },
                label = {
                    Text(
                        "Озвучка", style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(400),
                        )
                    )
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color(0xFF5E5E5E),
                    disabledLabelColor = Color(0xFFFFFFFF),
                    disabledTextColor = Color(0xFFFFFFFF),
                )
            )
        }

        // Season dropdown
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Show the dialog for selecting season
                if (selectedTranslation.id != -1) {
                    showDialog = "season"
                }
            }) {
            OutlinedTextField(
                value = "$seasonLocale ${selectedSeason?.id ?: "?"}",
                onValueChange = { /* Implement if needed */ },
                label = {
                    Text(
                        "Сезон", style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(400),
                        )
                    )
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color(0xFF5E5E5E),
                    disabledLabelColor = Color(0xFFFFFFFF),
                    disabledTextColor = Color(0xFFFFFFFF),
                )
            )
        }

        // Episode dropdown
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Show the dialog for selecting episode
                if (selectedSeason != null) {
                    showDialog = "episode"
                }
            }) {
            OutlinedTextField(
                value = "$episodeLocale ${selectedEpisode?.id ?: "?"}",
                onValueChange = { /* Implement if needed */ },
                label = {
                    Text(
                        "Эпизод", style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(400),
                        )
                    )
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color(0xFF5E5E5E),
                    disabledLabelColor = Color(0xFFFFFFFF),
                    disabledTextColor = Color(0xFFFFFFFF),
                )
            )
        }

        if (resolutions.isEmpty()) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = Color(0xFFFC315E),
                trackColor = Color(0x1AFC315E)
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Show the resolution buttons
                for (resolution in resolutions) {
                    Button(
                        onClick = { onResolutionSelected(resolution) },

                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (resolution == selectedResolution) {
                                Color(0xFFFFFFFF)
                            } else {
                                Color(0x1F767680)
                            }, contentColor = if (resolution == selectedResolution) {
                                Color(0xFF404040)
                            } else {
                                Color(0xFF909499)
                            }
                        )
                    ) {
                        Text(
                            resolution.quality, style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(400),
                            )
                        )
                    }
                }
            }
        }


        // Watch button
        Button(
            onClick = { onWatchClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp),
            shape = RoundedCornerShape(size = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFC315E),
                disabledContainerColor = Color(0xFFFC315E),
            ),
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
                tint = Color(0xFFFFFFFF)
            )
            Text(
                "Смотреть", style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center
                )
            )
        }

        if (showDialog != null) {
            // Show the corresponding dialog based on the showDialog value
            when (showDialog) {
                "season" -> {
                    CustomAlertDialog(title = "Выберите сезон", content = {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            if (selectedSeason != null) {
                                items(seasons) { season ->
                                    Text(text = "$seasonLocale ${season.id}",
                                        modifier = Modifier
                                            .clickable {
                                                // Select the season and update the selectedSeason
                                                onSeasonSelected(season)
                                                showDialog = null
                                            }
                                            .padding(16.dp)
                                            .fillMaxWidth())
                                }
                            }
                        }
                    }, onConfirm = {
                        if (seasons.isNotEmpty()) {
                            onSeasonSelected(seasons.first())
                        }
                    }, onDismiss = { showDialog = null })
                }

                "episode" -> {
                    CustomAlertDialog(title = "Выберите эпизод", content = {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            if (selectedSeason != null) {
                                items(selectedSeason.episodes) { episode ->
                                    val color = if (episode == selectedEpisode) {
                                        Color(0xFFFC315E)
                                    } else {
                                        Color(0xFFFFFFFF)
                                    }

                                    Row(modifier = Modifier
                                        .clickable {
                                            // Select the episode and update the selectedEpisode
                                            onEpisodeSelected(episode)
                                            showDialog = null
                                        }
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically) {
                                        // Show the episode name
                                        Text(
                                            text = "$episodeLocale ${episode.id}",
                                            modifier = Modifier.weight(1f),
                                            color = color
                                        )
                                    }
                                }
                            }
                        }
                    }, onConfirm = {
                        if (selectedSeason != null && selectedSeason.episodes.isNotEmpty()) {
                            onEpisodeSelected(selectedSeason.episodes.first())
                        }
                    }, onDismiss = { showDialog = null })
                }

                "translation" -> {
                    CustomAlertDialog(title = "Выберите озвучку", content = {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(movie.translations) { translation ->
                                Text(text = translation.name, modifier = Modifier
                                    .clickable {
                                        // Select the translation and update the selectedTranslation
                                        onTranslationSelected(translation)
                                        showDialog = null
                                    }
                                    .padding(16.dp)
                                    .fillMaxWidth())
                            }
                        }
                    }, onConfirm = {
                        onTranslationSelected(selectedTranslation)
                    }, onDismiss = { showDialog = null })
                }
            }
        }

    }
}