package com.example.filmus.ui.screens.favorites

import android.content.Context
import android.os.VibrationEffect
import android.os.VibratorManager
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.filmus.common.Constants
import com.example.filmus.domain.UIState
import com.example.filmus.repository.TokenManager
import com.example.filmus.ui.navigation.Screen
import com.example.filmus.ui.screens.favorites.poster.FavoritesList
import com.example.filmus.ui.screens.favorites.states.LoadingPlaceholder
import com.example.filmus.ui.screens.favorites.states.NoFavoritesPlaceholder
import com.example.filmus.viewmodel.favorites.FavoritesViewModel
import com.example.filmus.viewmodel.favorites.FavoritesViewModelFactory


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoritesScreen(
    navController: NavHostController, tokenManager: TokenManager
) {
    val viewModel: FavoritesViewModel = viewModel(factory = FavoritesViewModelFactory(tokenManager))
    val vibratorManager =
        LocalContext.current.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    val vibrator = vibratorManager.defaultVibrator
    LaunchedEffect(Unit) {
        val secondScreenResult =
            navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("newFavorite")?.value
        val secondScreenResult2 =
            navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("newReview")?.value
        if (((secondScreenResult != null) && (secondScreenResult == true)) || ((secondScreenResult2 != null) && (secondScreenResult2 == true))) {
            viewModel.getFavorites()
        }
    }
    val userReviews by viewModel.userReviews
    val movies by viewModel.movies
    val screenState by viewModel.screenState
    val pullRefreshState = rememberPullRefreshState(screenState == UIState.LOADING, {
        viewModel.getFavorites()
    })
    when (screenState) {
        UIState.UNAUTHORIZED -> {
            Toast.makeText(
                LocalContext.current, Constants.UNAUTHORIZED_ERROR, Toast.LENGTH_SHORT
            ).show()
            navController.navigate(
                Screen.Welcome.route
            ) {
                popUpTo(Screen.Welcome.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        UIState.ERROR -> {
            Toast.makeText(
                LocalContext.current, Constants.UNKNOWN_ERROR, Toast.LENGTH_SHORT
            ).show()
        }

        else -> {
            Box(Modifier.pullRefresh(pullRefreshState)) {
                if (screenState == UIState.LOADING) {
                    LoadingPlaceholder()
                } else {
                    if (movies.isEmpty()) {
                        NoFavoritesPlaceholder()
                    } else {
                        FavoritesList(
                            movies = movies,
                            userReviews = userReviews,
                            onCardClick = {
                                vibrator.vibrate(
                                    VibrationEffect.createOneShot(
                                        Constants.VIBRATION_BUTTON_CLICK,
                                        VibrationEffect.DEFAULT_AMPLITUDE
                                    )
                                )
                                navController.navigate("movie/${it}")
                            },
                            onDeleteClick = {
                                vibrator.vibrate(
                                    VibrationEffect.createOneShot(
                                        Constants.VIBRATION_BUTTON_CLICK,
                                        VibrationEffect.DEFAULT_AMPLITUDE
                                    )
                                )
                                viewModel.removeFavorite(it)
                            },
                        )
                    }
                }
                PullRefreshIndicator(
                    screenState == UIState.LOADING,
                    pullRefreshState,
                    Modifier.align(Alignment.TopCenter)
                )

            }
        }
    }
}





