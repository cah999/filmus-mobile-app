package com.example.filmus.ui.screens.movie.utils

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.filmus.domain.api.Api
import com.example.filmus.domain.api.MoviesList
import com.example.filmus.domain.api.Stream
import com.example.filmus.domain.api.Translation
import kotlinx.coroutines.launch

val api = Api()

class ApiError(message: String) : Exception(message)
class Duration(private val duration: String) {
    override fun toString(): String {
        return duration
    }
}

data class ExMovie(
    val id: Int,
    val path: String,
    val previewImageUrl: String,
    val duration: Duration,
    val translations: List<Translation>,
)

suspend fun performSearch(query: String, page: Int = 1): MoviesList {
    val searchResult = try {
        api.search(query = query, page = page)
    } catch (e: ApiError) {
        Log.d("Movie", "GOT ERROR HINTS ${e.message.toString()}")
        MoviesList(1, 1, emptyList())
    }

    return searchResult
}

suspend fun getHints(query: String): String? {
    if (query.isEmpty()) return null
    val result = try {
        api.searchAjax(query)
    } catch (e: ApiError) {
        Log.d("Movie", "GOT ERROR HINTS ${e.message.toString()}")
        null
    }
    return result
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchBottomSheet(
    showSheet: Boolean,
    onDismissSheet: () -> Unit,
    movieName: String
) {
    val movie = remember { mutableStateOf<ExMovie?>(null) }
    val trailerUrl = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        Log.d("Movie", "movieName = $movieName")
        val res1 = getHints(movieName)
        Log.d("Movie", "res1 = $res1")
//        val movie = api.getMovie(res.first().path)
//        Log.d("Movie", "movie = $movie")
        if (res1 != null)
            movie.value = api.getMovie(res1)
        Log.d("Movie", movie.value.toString())
        val resolutions = api.loadResolutions(
            movie.value!!.id,
            movie.value!!.translations.first().id,
            null,
            null
        )
        Log.d("Movie", "resolutions = $resolutions")
        val url = api.getTrailer(movie.value?.id ?: 0) ?: ""
        trailerUrl.value = url
        Log.d("Movie", "trailerUrl = $trailerUrl")
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val selectedResolution = remember { mutableStateOf<Stream?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Remember the selected translation, season, and episode states
    val selectedTranslation = remember { mutableStateOf(movie.value?.translations?.firstOrNull()) }
    Log.d("Movie", "show sheet = $showSheet")
    if (showSheet && movie.value != null) {
        // Only translation
        ModalBottomSheet(
            sheetState = sheetState,
            content = {
                // Pass the states and update functions to the WatchBottomSheetContent composable
                WatchBottomSheetContent(
                    movie = movie.value!!,
                    selectedTranslation = selectedTranslation.value
                        ?: movie.value!!.translations.first(),
                    onTranslationSelected = { translation: Translation ->
                        scope.launch {
                            selectedTranslation.value = translation
                        }
                    },
                    onCloseSheet = onDismissSheet,
                    onWatchClicked = {
                        Log.d("Movie", "selectedResolution = ${selectedResolution.value}")
                        if (selectedResolution.value == null) {
                            Toast.makeText(
                                context,
                                "Выберите качество",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@WatchBottomSheetContent
                        }
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(
                                Uri.parse(selectedResolution.value!!.url),
                                "video/mp4"
                            )

                            if (selectedResolution.value!!.subtitles.isNotEmpty()) {
                                // Pass the subtitles to the activity (Google Images, XPlore MediaView, MI Video Player, ExoPlayer)

                                // ExoPlayer
                                putExtra(
                                    "subtitle_uri",
                                    selectedResolution.value!!.subtitles.first().url
                                )
                                putExtra(
                                    "subtitle",
                                    selectedResolution.value!!.subtitles.first().url
                                )
                                putExtra(
                                    "subtitle_url",
                                    selectedResolution.value!!.subtitles.first().url
                                )
                                putExtra(
                                    "subtitle_language",
                                    selectedResolution.value!!.subtitles.first().lang
                                )
                                putExtra(
                                    "subtitle_label",
                                    selectedResolution.value!!.subtitles.first().name
                                )
                                putExtra(
                                    "subtitle_lang",
                                    selectedResolution.value!!.subtitles.first().lang
                                )

                                // MI Video Player
                                val subtitleList =
                                    selectedResolution.value!!.subtitles.map { subtitle ->
                                        subtitle.url
                                    }
                                putStringArrayListExtra(
                                    "subtitles",
                                    ArrayList(subtitleList)
                                )
                            }
                        }
                        Log.d("Movie", "intent = $intent")

                        if (intent.resolveActivity(context.packageManager) != null) {
                            Log.d("Movie", "start activity")
                            context.startActivity(intent)
                        } else {
                            //
                        }
                    },
                    selectedResolution = selectedResolution.value,
                    onResolutionSelected = { resolution: Stream ->
                        selectedResolution.value = resolution
                    },
                    onWatchTrailerClicked = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(trailerUrl.value)
                        )
                        context.startActivity(intent)
                    }
                )
            },
            scrimColor = Color.Black.copy(alpha = 0.5f),
            shape = MaterialTheme.shapes.large,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            onDismissRequest = onDismissSheet
        )
    }
}


@Composable
fun WatchBottomSheetContent(
    movie: ExMovie,
    selectedTranslation: Translation,
    onTranslationSelected: (Translation) -> Unit,
    onCloseSheet: () -> Unit,
    onWatchClicked: () -> Unit,
    onWatchTrailerClicked: () -> Unit,
    selectedResolution: Stream?,
    onResolutionSelected: (Stream) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
//    val api = Api()
    var resolutions by remember { mutableStateOf(listOf<Stream>()) }

    val context = LocalContext.current

    // Function to load resolutions for the current translation, season, and episode
    suspend fun loadResolutions() {
        // Implement the API call to load resolutions for the selected translation, season, and episode
        // For example:
        try {
            resolutions = api.loadResolutions(
                movie.id,
                selectedTranslation.id,
                null,
                null
            )
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

        // Once the resolutions are loaded, update the selectedResolution state with the first resolution (if any)
        if (resolutions.isNotEmpty()) {
            onResolutionSelected(resolutions.first())
        }
    }

    LaunchedEffect(selectedTranslation) {
        // Load resolutions when the selectedTranslation changes
        loadResolutions()
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Translation dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Toggle the menu visibility
                    showDialog = true
                }
        ) {
            OutlinedTextField(
                value = selectedTranslation.name,
                onValueChange = { /* Implement if needed */ },
                label = { Text("Translation") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onPrimary,
                    disabledBorderColor = MaterialTheme.colorScheme.primary,
                    disabledLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }

        // Resolution buttons in row with scrollable at horizontal
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Show the resolution buttons
            for (resolution in resolutions) {
                Log.d("Movie", "resolution: $resolution")
                Button(
                    onClick = { onResolutionSelected(resolution) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (resolution == selectedResolution) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        contentColor = if (resolution == selectedResolution) {
                            MaterialTheme.colorScheme.onSecondary
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        }
                    )
                ) {
                    Text(resolution.quality)
                }
            }
        }

        // Watch button
        Button(
            onClick = { onWatchClicked() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Watch")
        }

        Button(
            onClick = { onWatchTrailerClicked() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Trailer")
        }

        // Close button
        Button(
            onClick = onCloseSheet,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Close")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                confirmButton = {
                    // Implement the confirm button for the translation dialog
                    Button(
                        onClick = {
                            // Close the dialog and update the selectedTranslation
                            showDialog = false
                            onTranslationSelected(selectedTranslation)
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                title = {
                    Text("Select translation")
                },
                text = {
                    // Implement the list of translations in the dialog
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(movie.translations) { translation ->
                            Text(
                                text = translation.name,
                                modifier = Modifier
                                    .clickable {
                                        // Select the translation and update the selectedTranslation
                                        onTranslationSelected(translation)
                                        showDialog = false
                                    }
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                },
                dismissButton = {
                    // Implement the dismiss button for the translation dialog
                    Button(
                        onClick = {
                            // Close the dialog
                            showDialog = false
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            )
        }

    }
}