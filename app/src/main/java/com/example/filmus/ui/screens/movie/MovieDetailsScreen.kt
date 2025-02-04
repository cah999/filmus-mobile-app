package com.example.filmus.ui.screens.movie

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.VibrationEffect
import android.os.VibratorManager
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.filmus.R
import com.example.filmus.common.Constants
import com.example.filmus.domain.UIState
import com.example.filmus.repository.TokenManager
import com.example.filmus.ui.marks.FilmMark
import com.example.filmus.ui.navigation.Screen
import com.example.filmus.ui.screens.movie.utils.InfoRow
import com.example.filmus.ui.screens.movie.utils.MovieDetailsPlaceholder
import com.example.filmus.ui.screens.movie.utils.ReviewCard
import com.example.filmus.ui.screens.movie.utils.ReviewDialog
import com.example.filmus.ui.screens.movie.utils.verticalFadingEdge
import com.example.filmus.ui.screens.movie.watch.WatchBottomSheet
import com.example.filmus.viewmodel.movie.MovieViewModel
import com.example.filmus.viewmodel.movie.MovieViewModelFactory

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class
)
@Composable
fun MovieDetailsScreen(
    movieId: String, navController: NavHostController, tokenManager: TokenManager
) {
    val viewModel: MovieViewModel =
        viewModel(factory = MovieViewModelFactory(movieId, tokenManager))
    val movie by viewModel.movieDetails
    var isFavorite by viewModel.isFavorite
    val vibratorManager =
        LocalContext.current.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    val vibrator = vibratorManager.defaultVibrator
    val lazyState = rememberLazyListState()
    val newReview by viewModel.newReview
    val newFavorite by viewModel.newFavorite
    navController.previousBackStackEntry?.savedStateHandle?.set(
        "newReview", newReview
    )
    navController.previousBackStackEntry?.savedStateHandle?.set(
        "newFavorite", newFavorite
    )
    val fadingEdgeModifier = Modifier.verticalFadingEdge(
        lazyState, length = 200.dp, edgeColor = Color(0xFF1D1D1D)
    )
    var showDialog by remember { mutableStateOf(false) }
    val existsReviewID by viewModel.existsReviewID
    var showSheet by remember { mutableStateOf(false) }
    var isShaking by remember { mutableStateOf(false) }
    val sensorManager =
        LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    DisposableEffect(isShaking) {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val acceleration = kotlin.math.sqrt(x * x + y * y + z * z)

                if (acceleration > 20) {
                    isShaking = true
                    showSheet = true
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            100, VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }

        sensorManager.registerListener(
            sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }
    var isExpanded by remember { mutableStateOf(false) }
    val maxLines = if (isExpanded) Int.MAX_VALUE else 4

    val hasScrolledToTitle by remember {
        derivedStateOf {
            lazyState.firstVisibleItemIndex > 2
        }
    }
    val imagePoster = rememberAsyncImagePainter(model = movie?.poster ?: "")
    when (viewModel.screenState.value) {
        UIState.UNAUTHORIZED -> {
            Toast.makeText(
                LocalContext.current, Constants.UNAUTHORIZED_ERROR, Toast.LENGTH_SHORT
            ).show()
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Main.route) { inclusive = true }
            }
            viewModel.screenState.value = UIState.DEFAULT
        }

        UIState.ERROR -> {
            Toast.makeText(
                LocalContext.current, viewModel.errorMessage.value, Toast.LENGTH_SHORT
            ).show()
            viewModel.screenState.value = UIState.DEFAULT
        }

        else -> {}

    }
    Scaffold(containerColor = Color(0xFF1D1D1D), topBar = {
        CenterAlignedTopAppBar(colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFF1D1D1D),
            navigationIconContentColor = Color(0xFFFFFFFF),
        ), title = {}, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = null,
                )
            }
        }, actions = {})
    }) { it ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .then(fadingEdgeModifier)
                .padding(it),
            state = lazyState
        ) {
            if ((movie == null) || (viewModel.screenState.value == UIState.LOADING)) {
                item {
                    MovieDetailsPlaceholder()
                }
            } else {
                item {
                    Image(painter = imagePoster,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(497.dp)
                            .drawWithContent {
                                val gradient = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color(0xFF1D1D1D)
                                    ), startY = size.height / 3, endY = size.height
                                )
                                drawContent()
                                drawRect(
                                    brush = gradient, blendMode = BlendMode.SrcOver
                                )

                            }
                            .padding(0.dp)
                            .combinedClickable(onClick = { }, onLongClick = {
                                vibrator.vibrate(
                                    VibrationEffect.createOneShot(
                                        Constants.VIBRATION_BUTTON_CLICK,
                                        VibrationEffect.DEFAULT_AMPLITUDE
                                    )
                                )
                                showSheet = true
                            }),
                        contentScale = ContentScale.Crop
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilmMark(
                            movie!!.reviews.map { it?.rating ?: 0 }.average().toFloat(),
                            modifier = Modifier
                                .width(51.dp)
                                .height(26.dp),
                        )
                        (if (movie!!.name == null) "Фильм" else movie!!.name)?.let { title ->
                            Text(
                                text = title, style = TextStyle(
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily(Font(R.font.inter)),
                                    fontWeight = FontWeight(700),
                                    color = Color(0xFFFFFFFF),
                                    textAlign = TextAlign.Center,
                                ), modifier = Modifier.width(212.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                isFavorite = !isFavorite
                                vibrator.vibrate(
                                    VibrationEffect.createOneShot(
                                        Constants.VIBRATION_BUTTON_CLICK,
                                        VibrationEffect.DEFAULT_AMPLITUDE
                                    )
                                )
                                if (isFavorite) viewModel.addFavorite(movieId)
                                else viewModel.removeFavorite(movieId)
                            },
                            modifier = Modifier
                                .background(Color(0xFF404040), shape = CircleShape)
                                .width(40.dp)
                                .height(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isFavorite) Color(0xFFFC315E) else Color(0xFFFFFFFF),
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp)
                            )
                        }

                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    AnimatedVisibility(visible = !isExpanded) {
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    (if (movie!!.description == null) "Описание" else movie!!.description)?.let { title ->
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFFCCCCCC),
                            ),
                            maxLines = maxLines,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp)
                                .drawWithCache {
                                    val gradient = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            if (isExpanded) Color.Transparent else Color(
                                                0xFF1D1D1D
                                            )
                                        ), startY = size.height / 2, endY = size.height
                                    )
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(gradient, blendMode = BlendMode.Darken)
                                    }
                                },
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                vibrator.vibrate(
                                    VibrationEffect.createOneShot(
                                        Constants.VIBRATION_BUTTON_CLICK,
                                        VibrationEffect.DEFAULT_AMPLITUDE
                                    )
                                )
                                isExpanded = !isExpanded
                            }
                            .padding(start = 16.dp, end = 16.dp)) {
                        Text(
                            text = "Подробнее", style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFFFC315E),
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color(0xFFFC315E)
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    Text(
                        text = "Жанры", style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFFFFFFF),
                        ), modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        for (genre in movie!!.genres) {
                            Text(
                                text = genre?.name ?: "Жанр", style = TextStyle(
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily(Font(R.font.inter)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFFFFFFFF),

                                    textAlign = TextAlign.Center,
                                ), modifier = Modifier
                                    .background(
                                        color = Color(0xFFFC315E),
                                        shape = RoundedCornerShape(size = 5.dp)
                                    )
                                    .padding(
                                        start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp
                                    )
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    Text(
                        text = "О фильме", style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFFFFFFF),
                        ), modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )

                    val infoModifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 16.dp, end = 16.dp)


                    InfoRow("Год", movie!!.year.toString(), infoModifier)
                    if (movie!!.country != null) {
                        movie!!.country?.let { title -> InfoRow("Страна", title, infoModifier) }
                    }
                    if (movie!!.tagline != null) {
                        movie!!.tagline?.let { title -> InfoRow("Слоган", title, infoModifier) }
                    }
                    if (movie!!.director != null) {
                        movie!!.director?.let { title ->
                            InfoRow(
                                "Режиссёр", title, infoModifier
                            )
                        }
                    }
                    if (movie!!.budget != null) {
                        movie!!.budget?.let { title ->
                            InfoRow(
                                "Бюджет", "\$${title}", infoModifier
                            )
                        }
                    }
                    if (movie!!.fees != null) {
                        movie!!.fees?.let { title ->
                            InfoRow(
                                "Сборы в мире", "\$${title}", infoModifier
                            )
                        }
                    }
                    InfoRow(
                        "Возраст", "${movie!!.ageLimit}+", infoModifier
                    )
                    InfoRow(
                        "Время", "${movie!!.time} мин.", infoModifier
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        Text(
                            text = "Отзывы", style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(700),
                                color = Color(0xFFFFFFFF),

                                )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (existsReviewID == null) IconButton(
                            onClick = { showDialog = true },
                            modifier = Modifier
                                .background(Color(0xFFFC315E), shape = CircleShape)
                                .width(32.dp)
                                .height(32.dp)
                        ) {
                            Icon(
                                painterResource(id = R.drawable.plus),
                                contentDescription = null,
                                tint = Color(0xFFFFFFFF),
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp)
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(15.dp))
                }
                if (movie?.reviews?.isNotEmpty() == true) {
                    if (existsReviewID != null) {
                        val review = movie!!.reviews.find { it?.id == existsReviewID }
                        if (review != null) {
                            item {
                                ReviewCard(
                                    review = review,
                                    isUser = review.id in viewModel.userReviews,
                                    onEdit = {
                                        vibrator.vibrate(
                                            VibrationEffect.createOneShot(
                                                Constants.VIBRATION_BUTTON_CLICK,
                                                VibrationEffect.DEFAULT_AMPLITUDE
                                            )
                                        )
                                        viewModel.review.value = review.reviewText ?: ""
                                        viewModel.rating.intValue = review.rating
                                        viewModel.isAnonymous.value = review.isAnonymous
                                        viewModel.reviewID.value = review.id
                                        showDialog = true
                                    },
                                    onDelete = {
                                        vibrator.vibrate(
                                            VibrationEffect.createOneShot(
                                                Constants.VIBRATION_BUTTON_CLICK,
                                                VibrationEffect.DEFAULT_AMPLITUDE
                                            )
                                        )
                                        viewModel.removeReview(
                                            review.id,
                                        )
                                    },
                                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }

                    }
                    items(movie!!.reviews.filter { review ->
                        existsReviewID == null || review?.id != existsReviewID
                    }) { review ->
                        if (review != null) {
                            ReviewCard(
                                review = review,
                                isUser = review.id in viewModel.userReviews,
                                onEdit = {
                                    viewModel.review.value = review.reviewText ?: ""
                                    viewModel.rating.intValue = review.rating
                                    viewModel.isAnonymous.value = review.isAnonymous
                                    viewModel.reviewID.value = review.id
                                    showDialog = true
                                },
                                onDelete = {
                                    viewModel.removeReview(
                                        review.id,
                                    )
                                },
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                } else {
                    item {
                        Text(
                            text = "Нет отзывов",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(50),
                                color = Color(0xFFFFFFFF),

                                ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp)
                        )
                    }
                }
            }

        }
    }

    if (movie != null && hasScrolledToTitle) {
        Column {
            CenterAlignedTopAppBar(colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color(0xFF1D1D1D),
                navigationIconContentColor = Color(0xFFFFFFFF),
            ), title = {
                (if (movie!!.name == null) "Фильм" else movie!!.name)?.let {
                    Text(
                        text = it, style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFFFFFFF),

                            ), textAlign = TextAlign.Center, maxLines = 2
                    )
                }
            }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painterResource(id = R.drawable.back), contentDescription = null
                    )
                }
            }, actions = {
                Box(Modifier.padding(end = 15.dp), contentAlignment = Alignment.CenterEnd) {
                    IconButton(
                        onClick = {
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(
                                    Constants.VIBRATION_BUTTON_CLICK,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            )
                            isFavorite = !isFavorite
                            if (isFavorite) viewModel.addFavorite(movieId)
                            else viewModel.removeFavorite(movieId)
                        },
                        modifier = Modifier
                            .background(Color(0xFF404040), shape = CircleShape)
                            .width(40.dp)
                            .height(40.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFavorite) Color(0xFFFC315E) else Color(0xFFFFFFFF),
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                        )
                    }
                }
            })
            Divider(
                modifier = Modifier.fillMaxWidth(), color = Color(0xA6545458), thickness = 1.dp
            )
        }
    }
    if (showDialog) {
        ReviewDialog(onDismissRequest = { showDialog = false },
            onClick = {
                if (existsReviewID != null) viewModel.editReview() else viewModel.addReview()
            },
            rating = viewModel.rating.intValue,
            onRatingChanged = { viewModel.rating.intValue = it },
            reviewText = viewModel.review.value,
            onReviewChanged = { viewModel.review.value = it },
            isAnonymous = viewModel.isAnonymous.value,
            onIsAnonymousChanged = { viewModel.isAnonymous.value = it })
    }
    if (movie != null) WatchBottomSheet(
        showSheet = showSheet,
        isShaking = isShaking,
        onDismissSheet = {
            showSheet = false
            isShaking = false
        },
        movie = viewModel.watchMovie.value,
        trailerUrl = viewModel.movieTrailer.value,
        onLoadResolutions = viewModel.provideLoadResolutions(),
        onLoadSeasonsForTranslation = viewModel.provideLoadSeasonsForTranslation(),
    )
}


