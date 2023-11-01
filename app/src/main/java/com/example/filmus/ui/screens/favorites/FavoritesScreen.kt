package com.example.filmus.ui.screens.favorites

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.filmus.domain.favorite.Poster
import com.example.filmus.viewmodel.favorites.FavoritesViewModel


@Composable
fun FavoritesScreen(
    posters: List<Poster>, navController: NavController, viewModel: FavoritesViewModel
) {
    if (posters.isEmpty()) {
        NoFavoritesPlaceholder()
    } else {
        FavoritesList(posters = posters)
    }


}





