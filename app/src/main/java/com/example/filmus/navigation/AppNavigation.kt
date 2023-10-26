package com.example.filmus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.filmus.R
import com.example.filmus.ui.screens.FavoritesScreen
import com.example.filmus.ui.screens.LoginScreen
import com.example.filmus.ui.screens.Poster
import com.example.filmus.ui.screens.RegistrationPwdScreen
import com.example.filmus.ui.screens.RegistrationScreen
import com.example.filmus.ui.screens.WelcomeScreen
import com.example.filmus.viewmodel.login.LoginViewModel
import com.example.filmus.viewmodel.registration.RegistrationViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    appNavigator: AppNavigator,
    loginViewModel: LoginViewModel,
    registrationViewModel: RegistrationViewModel
) {
    NavHost(navController, startDestination = appNavigator.currentScreen.value.route) {
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
            val posters = listOf(
                Poster(R.drawable.splash_background, "Постер 1"),
                Poster(R.drawable.ic_launcher_background, "Постер 2"),
                Poster(R.drawable.splash_background, "Постер 3"),
                Poster(R.drawable.splash_background, "Постер 3"),
                Poster(R.drawable.ic_launcher_background, "Постер 3"),
                Poster(R.drawable.back, "Постер 3"),
                Poster(R.drawable.back, "Постер 3"),
            )
            FavoritesScreen(posters = posters)
        }
    }
}
