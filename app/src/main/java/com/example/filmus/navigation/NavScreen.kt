package com.example.filmus.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Welcome : Screen("welcome", "Welcome", null)
    object Login : Screen("login", "Login", null)
    object Registration : Screen("registration", "Registration", null)
    object RegistrationPwd : Screen("registrationPwd", "RegistrationPwd", null)
    object Main : Screen("main", "Main", null)
    object Movie : Screen("movie", "Movie", Icons.Default.Search)

    object Favorite : Screen("favorite", "Favorite", Icons.Default.Notifications)

    object Profile : Screen("profile", "Profile", Icons.Default.Person)

}