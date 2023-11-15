package com.example.filmus.ui.navigation

import com.example.filmus.R

sealed class Screen(val route: String, val title: String, val imageResource: Int? = null) {
    data object Loading : Screen("loading", "Loading", null)
    data object Welcome : Screen("welcome", "Welcome", null)
    data object Login : Screen("login", "Login", null)
    data object Registration : Screen("registration", "Registration", null)
    data object RegistrationPwd : Screen("registrationPwd", "RegistrationPwd", null)
    data object Main : Screen("main", "Главная", R.drawable.home)

    data object Movie : Screen("movie", "Детали фильма", null)

    data object Favorite : Screen("favorite", "Любимое", R.drawable.favorite)

    data object Profile : Screen("profile", "Профиль", R.drawable.person)
}
