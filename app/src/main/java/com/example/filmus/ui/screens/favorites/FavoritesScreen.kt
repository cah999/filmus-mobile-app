package com.example.filmus.ui.screens.favorites

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.filmus.domain.favorite.Poster
import com.example.filmus.viewmodel.favorites.FavoritesViewModel
import com.example.filmus.viewmodel.favorites.FavoritesViewModelFactory


@Composable
fun FavoritesScreen(
    posters: List<Poster>, navController: NavController
) {
    val viewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory()
    )
    if (posters.isEmpty()) {
        NoFavoritesPlaceholder()
    } else {
        FavoritesList(posters = posters)
    }
}





