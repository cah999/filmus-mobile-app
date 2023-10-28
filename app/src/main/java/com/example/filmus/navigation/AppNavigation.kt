package com.example.filmus.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.filmus.R
import com.example.filmus.ui.screens.favorites.FavoritesScreen
import com.example.filmus.ui.screens.favorites.Poster
import com.example.filmus.ui.screens.login.LoginScreen
import com.example.filmus.ui.screens.main.MainScreen
import com.example.filmus.ui.screens.profile.ProfileScreen
import com.example.filmus.ui.screens.registration.RegistrationPwdScreen
import com.example.filmus.ui.screens.registration.RegistrationScreen
import com.example.filmus.ui.screens.splash.LoadingScreen
import com.example.filmus.ui.screens.welcome.WelcomeScreen
import com.example.filmus.viewmodel.login.LoginViewModel
import com.example.filmus.viewmodel.registration.RegistrationViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registrationViewModel: RegistrationViewModel,
    startScreen: Screen
) {
    NavHost(
        navController,
        startDestination = startScreen.route,
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }

    ) {
        composable(Screen.Loading.route) {
            LoadingScreen()
        }
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }
        composable(Screen.Registration.route) {
            RegistrationScreen(navController = navController, viewModel = registrationViewModel)
        }
        composable(Screen.RegistrationPwd.route) {
            RegistrationPwdScreen(navController = navController, viewModel = registrationViewModel)
        }
        composable(Screen.Favorite.route) {
            val posters: List<Poster> = listOf(
                Poster(R.drawable.splash_background, "Постер 1", 9),
                Poster(R.drawable.ic_launcher_background, "Постер 2", 3),
                Poster(R.drawable.splash_background, "Постер 3"),
                Poster(R.drawable.ic_launcher_background, "Постер 4", 4),
            )
            FavoritesScreen(posters = posters, navController = navController)
        }
        composable(Screen.Main.route) {
            MainScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}
