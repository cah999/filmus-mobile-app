package com.example.filmus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.filmus.ui.screens.LoginScreen
import com.example.filmus.ui.screens.ProfileScreen
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
        composable(Screen.Profile.route) {
            appNavigator.currentScreen.value = Screen.Profile
            ProfileScreen(navController = navController, appNavigator = appNavigator)
        }
    }
}
