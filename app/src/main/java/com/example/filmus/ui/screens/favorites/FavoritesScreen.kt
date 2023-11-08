package com.example.filmus.ui.screens.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.filmus.domain.UIState
import com.example.filmus.domain.UserManager
import com.example.filmus.viewmodel.favorites.FavoritesViewModel
import com.example.filmus.viewmodel.favorites.FavoritesViewModelFactory


@Composable
fun FavoritesScreen(
    navController: NavController, userManager: UserManager
) {
    val viewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(userManager)
    )
    LaunchedEffect(Unit) {
        viewModel.getFavorites()
        viewModel.getProfileReviews()
    }
    val userReviews = viewModel.userReviews
    val movies = viewModel.movies
    val screenState = viewModel.screenState
    if (screenState.value == UIState.LOADING) {
        LoadingPlaceholder()
    } else {
        if (movies.isEmpty()) {
            NoFavoritesPlaceholder()
        } else {
            FavoritesList(movies = movies, userReviews.value, navController, viewModel)
        }
    }
}





