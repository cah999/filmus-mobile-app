package com.example.filmus.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.filmus.api.ApiService
import com.example.filmus.ui.screens.LoginScreen
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
    }
}
