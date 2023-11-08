package com.example.filmus.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.filmus.domain.UserManager
import com.example.filmus.ui.screens.favorites.FavoritesScreen
import com.example.filmus.ui.screens.login.LoginScreen
import com.example.filmus.ui.screens.main.MainScreen
import com.example.filmus.ui.screens.movie.MovieDetailsScreen
import com.example.filmus.ui.screens.profile.ProfileScreen
import com.example.filmus.ui.screens.registration.RegistrationPwdScreen
import com.example.filmus.ui.screens.registration.RegistrationScreen
import com.example.filmus.ui.screens.splash.LoadingScreen
import com.example.filmus.ui.screens.welcome.WelcomeScreen
import com.example.filmus.viewmodel.registration.RegistrationViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    userManager: UserManager,
    registrationViewModel: RegistrationViewModel,
    startScreen: Screen
) {
    NavHost(navController,
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
            LoginScreen(navController = navController, userManager = userManager)
        }
        composable(Screen.Registration.route) {
            RegistrationScreen(navController = navController, viewModel = registrationViewModel)
        }
        composable(Screen.RegistrationPwd.route) {
            RegistrationPwdScreen(navController = navController, viewModel = registrationViewModel)
        }
        composable(Screen.Favorite.route) {
            FavoritesScreen(
                navController = navController, userManager = userManager
            )
        }
        composable(Screen.Main.route) {
            MainScreen(navController = navController, userManager = userManager)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController, userManager)
        }
        composable(
            "${Screen.Movie.route}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            MovieDetailsScreen(
                movieId = backStackEntry.arguments?.getString("movieId") ?: "",
                navController = navController,
                userManager = userManager
            )
        }
    }
}
