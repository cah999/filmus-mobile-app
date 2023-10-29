package com.example.filmus.navigation

import com.example.filmus.R

sealed class Screen(val route: String, val title: String, val imageResource: Int? = null) {
    object Loading : Screen("loading", "Loading", null)
    object Welcome : Screen("welcome", "Welcome", null)
    object Login : Screen("login", "Login", null)
    object Registration : Screen("registration", "Registration", null)
    object RegistrationPwd : Screen("registrationPwd", "RegistrationPwd", null)
    object Main : Screen("main", "Главная", R.drawable.home)
    object Movie : Screen("movie", "Movie", null)

    object Favorite : Screen("favorite", "Любимое", R.drawable.favorite)

    object Profile : Screen("profile", "Профиль", R.drawable.person)
}
