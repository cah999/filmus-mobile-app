package com.example.filmus.navigation

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
import com.example.filmus.ui.screens.welcome.WelcomeScreen
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
            appNavigator.currentScreen.value = Screen.Welcome
            WelcomeScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            appNavigator.currentScreen.value = Screen.Login
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }
        composable(Screen.Registration.route) {
            appNavigator.currentScreen.value = Screen.Registration
            RegistrationScreen(navController = navController, viewModel = registrationViewModel)
        }
        composable(Screen.RegistrationPwd.route) {
            appNavigator.currentScreen.value = Screen.RegistrationPwd
            RegistrationPwdScreen(navController = navController, viewModel = registrationViewModel)
        }
        composable(Screen.Favorite.route) {
            appNavigator.currentScreen.value = Screen.Favorite
            val posters = listOf(
                Poster(R.drawable.splash_background, "Постер 1", 9),
                Poster(R.drawable.ic_launcher_background, "Постер 2", 3),
                Poster(R.drawable.splash_background, "Постер 3"),
                Poster(R.drawable.ic_launcher_background, "Постер 4", 4),
            )
            FavoritesScreen(
                posters = posters,
                navController = navController,
                appNavigator = appNavigator
            )
        }
        composable(Screen.Main.route) {
            appNavigator.currentScreen.value = Screen.Main
            MainScreen(navController = navController, appNavigator = appNavigator)
        }
        composable(Screen.Profile.route) {
            appNavigator.currentScreen.value = Screen.Profile
            ProfileScreen(navController = navController, appNavigator = appNavigator)
        }
    }
}
