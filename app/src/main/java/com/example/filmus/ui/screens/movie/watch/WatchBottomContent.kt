package com.example.filmus.ui.screens.movie.watch

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.filmus.domain.movieAPI.ExMovie
import com.example.filmus.domain.movieAPI.Stream
import com.example.filmus.domain.movieAPI.Translation
import kotlinx.coroutines.launch


@Composable
fun WatchBottomSheetContent(
    movie: ExMovie,
    selectedTranslation: Translation,
    onTranslationSelected: (Translation) -> Unit,
    onWatchClicked: () -> Unit,
    selectedResolution: Stream?,
    onResolutionSelected: (Stream) -> Unit,
    onLoadResolutions: suspend (Int, Translation, Int?, Int?) -> List<Stream>,
) {
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var resolutions by remember { mutableStateOf(emptyList<Stream>()) }

    val context = LocalContext.current

    // Function to load resolutions for the current translation, season, and episode
    fun loadResolutions() {
        // Implement the API call to load resolutions for the selected translation
        // For example:
        scope.launch {
            try {
                resolutions = onLoadResolutions(movie.id, selectedTranslation, null, null)
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }

            // Once the resolutions are loaded, update the selectedResolution state with the first resolution (if any)
            if (resolutions.isNotEmpty()) {
                onResolutionSelected(resolutions.first())
            }
        }
    }

    // Function to load resolutions for the selected translation, season, and episode
    LaunchedEffect(selectedTranslation) {
        loadResolutions()
    }
    Column(
        modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Translation
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDialog = true
            }) {
            OutlinedTextField(
                value = selectedTranslation.name,
                onValueChange = {},
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

        if (resolutions.isEmpty()) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = Color(0xFFFC315E),
                trackColor = Color(0x1AFC315E)
            )
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Show the resolution buttons
                items(resolutions) { resolution ->
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

        if (showDialog) {
            CustomAlertDialog(title = "Выберите озвучку",
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(movie.translations) { translation ->
                            Text(text = translation.name, modifier = Modifier
                                .clickable {
                                    // Select the translation and update the selectedTranslation
                                    onTranslationSelected(translation)
                                    showDialog = false
                                }
                                .padding(16.dp)
                                .fillMaxWidth())
                        }
                    }
                },
                onConfirm = { onTranslationSelected(selectedTranslation) },
                onDismiss = { showDialog = false })

        }

    }
}


@Composable
fun CustomAlertDialog(
    title: String,
    content: @Composable () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(shape = MaterialTheme.shapes.medium,
        containerColor = Color(0xFF161616),
        textContentColor = Color(0xFFFFFFFF),
        titleContentColor = Color(0xFFFFFFFF),
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            // Implement the confirm button for the translation dialog
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                shape = RoundedCornerShape(size = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFC315E),
                    disabledContainerColor = Color(0xFFFC315E),
                ),
            ) {
                Text(
                    "Подтвердить", style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    )
                )
            }
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        title = {
            Text(
                title, style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                )
            )
        },
        text = {
            content()
        },
        dismissButton = {})

}