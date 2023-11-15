package com.example.filmus.ui.screens.movie.watch

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.filmus.common.Constants
import com.example.filmus.domain.movieAPI.Episode
import com.example.filmus.domain.movieAPI.ExMovie
import com.example.filmus.domain.movieAPI.Season
import com.example.filmus.domain.movieAPI.Stream
import com.example.filmus.domain.movieAPI.Translation
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchBottomSheet(
    showSheet: Boolean,
    isShaking: Boolean,
    onDismissSheet: () -> Unit,
    movie: ExMovie?,
    trailerUrl: String,
    onLoadResolutions: suspend (Int, Translation, Int?, Int?) -> List<Stream>,
    onLoadSeasonsForTranslation: suspend (Int, Int) -> List<Season>,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val selectedResolution = remember { mutableStateOf<Stream?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val selectedTranslation = remember { mutableStateOf(movie?.translations?.firstOrNull()) }
    if (movie == null) {
        if (showSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                content = {
                    WatchBottomLoader()
                },
                scrimColor = Color.Black.copy(alpha = 0.75f),
                shape = MaterialTheme.shapes.medium,
                containerColor = Color(0xFF161616),
                contentColor = Color(0xFFFFFFFF),
                onDismissRequest = onDismissSheet
            )
        }
    } else {
        if (movie.isSerial) {
            val selectedSeason = remember { mutableStateOf<Season?>(null) }
            val selectedEpisode = remember { mutableStateOf<Episode?>(null) }
            val seasons = remember { mutableStateOf(listOf<Season>()) }

            // Function to load seasons for the selected translation
            suspend fun loadSeasonsForSelectedTranslation(translation: Translation): Job {
                selectedSeason.value = null // Reset selected season when translation changes
                // Load episodes for the first season of the selected translation
                return scope.launch {
                    seasons.value = try {
                        onLoadSeasonsForTranslation(movie.id, translation.id)
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        emptyList()
                    }
                    if (seasons.value.isNotEmpty()) {
                        selectedSeason.value = seasons.value.first()
                        if (selectedSeason.value!!.episodes.isNotEmpty()) {
                            selectedEpisode.value = selectedSeason.value!!.episodes.first()
                        }
                    }
                }
            }

            LaunchedEffect(Unit) {
                // Load seasons for the selected translation
                if (movie.translations.isEmpty()) {
                    Toast.makeText(
                        context,
                        Constants.TRANSLATIONS_NOT_FOUND,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@LaunchedEffect
                }

                loadSeasonsForSelectedTranslation(
                    selectedTranslation.value ?: movie.translations.first()
                ).join()
            }

            if (showSheet) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    content = {
                        WatchBottomSheetContentSerial(
                            movie = movie,
                            selectedTranslation = selectedTranslation.value
                                ?: movie.translations.first(),
                            onTranslationSelected = { translation ->
                                scope.launch {
                                    selectedTranslation.value = translation
                                }
                            },
                            selectedSeason = selectedSeason.value,
                            onSeasonSelected = { season ->
                                scope.launch {
                                    selectedSeason.value = season
                                    selectedEpisode.value = season.episodes.firstOrNull()
                                }
                            },
                            selectedEpisode = selectedEpisode.value,
                            onEpisodeSelected = { episode ->
                                selectedEpisode.value = episode
                            },
                            onWatchClicked = {
                                onWatchClicked(selectedResolution.value, context)
                            },
                            selectedResolution = selectedResolution.value,
                            onResolutionSelected = { resolution ->
                                selectedResolution.value = resolution
                            },
                            onLoadResolutions = onLoadResolutions,
                            onLoadSeasonsForTranslation = onLoadSeasonsForTranslation
                        )
                    },
                    scrimColor = Color.Black.copy(alpha = 0.75f),
                    shape = MaterialTheme.shapes.medium,
                    containerColor = Color(0xFF161616),
                    contentColor = Color(0xFFFFFFFF),
                    onDismissRequest = onDismissSheet
                )
            }
        } else {
            if (showSheet) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    content = {
                        if (isShaking) {
                            WatchBottomTrailer(
                                onWatchTrailerClicked = {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW, Uri.parse(trailerUrl)
                                    )
                                    context.startActivity(intent)
                                },
                            )
                        } else {
                            WatchBottomSheetContent(
                                movie = movie,
                                selectedTranslation = selectedTranslation.value
                                    ?: movie.translations.first(),
                                onTranslationSelected = { translation: Translation ->
                                    scope.launch {
                                        selectedTranslation.value = translation
                                    }
                                },
                                onWatchClicked = {
                                    onWatchClicked(selectedResolution.value, context)
                                },
                                selectedResolution = selectedResolution.value,
                                onResolutionSelected = { resolution: Stream ->
                                    selectedResolution.value = resolution
                                },
                                onLoadResolutions = onLoadResolutions
                            )
                        }
                    },
                    scrimColor = Color.Black.copy(alpha = 0.75f),
                    shape = MaterialTheme.shapes.medium,
                    containerColor = Color(0xFF161616),
                    contentColor = Color(0xFFFFFFFF),
                    onDismissRequest = onDismissSheet
                )
            }
        }
    }
}

private fun onWatchClicked(selectedResolution: Stream?, context: Context) {
    if (selectedResolution == null) {
        Toast.makeText(
            context, "Выберите качество", Toast.LENGTH_SHORT
        ).show()
        return
    }
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(
            Uri.parse(selectedResolution.url),
            "video/mp4"
        )

        if (selectedResolution.subtitles.isNotEmpty()) {
            // Pass the subtitles to the activity (Google Images, XPlore MediaView, MI Video Player, ExoPlayer)

            // ExoPlayer
            putExtra(
                "subtitle_uri",
                selectedResolution.subtitles.first().url
            )
            putExtra(
                "subtitle",
                selectedResolution.subtitles.first().url
            )
            putExtra(
                "subtitle_url",
                selectedResolution.subtitles.first().url
            )
            putExtra(
                "subtitle_language",
                selectedResolution.subtitles.first().lang
            )
            putExtra(
                "subtitle_label",
                selectedResolution.subtitles.first().name
            )
            putExtra(
                "subtitle_lang",
                selectedResolution.subtitles.first().lang
            )

            // MI Video Player
            val subtitleList =
                selectedResolution.subtitles.map { subtitle ->
                    subtitle.url
                }
            putStringArrayListExtra(
                "subtitles", ArrayList(subtitleList)
            )
        }
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        //
    }
}
