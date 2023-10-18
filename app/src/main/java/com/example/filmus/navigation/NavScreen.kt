package com.example.filmus.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Welcome : Screen("welcome", "Welcome", Icons.Default.Home)
    object Login : Screen("login", "Login", Icons.Default.Person)
    object Registration : Screen("registration", "Registration", Icons.Default.Person)
    object RegistrationPwd : Screen("registrationPwd", "RegistrationPwd", Icons.Default.Person)
    object Main : Screen("main", "Main", Icons.Default.Home)
    object Movie : Screen("movie", "Movie", Icons.Default.Search)

    object Favorite : Screen("favorite", "Favorite", Icons.Default.Notifications)

    object Profile : Screen("profile", "Profile", Icons.Default.Person)

}