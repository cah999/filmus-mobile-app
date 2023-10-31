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
import com.example.filmus.ui.screens.main.Movie
import com.example.filmus.ui.screens.main.Review
import com.example.filmus.ui.screens.profile.ProfileScreen
import com.example.filmus.ui.screens.registration.RegistrationPwdScreen
import com.example.filmus.ui.screens.registration.RegistrationScreen
import com.example.filmus.ui.screens.splash.LoadingScreen
import com.example.filmus.ui.screens.welcome.WelcomeScreen
import com.example.filmus.viewmodel.login.LoginViewModel
import com.example.filmus.viewmodel.mainscreen.MovieViewModel
import com.example.filmus.viewmodel.registration.RegistrationViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registrationViewModel: RegistrationViewModel,
    movieViewModel: MovieViewModel,
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
            val movies = listOf(
                Movie(
                    "Название фильма 1",
                    R.drawable.splash_background,
                    2021,
                    "Россия",
                    listOf("драма", "комедия", "биография", "криминал", "боевик", "триллер"),
                    listOf(
                        Review("1", 8),
                        Review("2", 8),
                        Review("3", 8),
                        Review("4", 9),
                        Review("5", 8),
                    )
                ),
                Movie(
                    "Какое то очень большое Название фильма 2",
                    R.drawable.ic_launcher_background,
                    2023,
                    "США",
                    listOf("Жанр 1", "Жанр 2", "Жанр 3", "Жанр 4", "Жанр 5", "Жанр 6"),
                    listOf(
                        Review("1", 5),
                        Review("2", 4),
                        Review("3", 3),
                        Review("4", 2),
                        Review("5", 1),
                    )
                ),                Movie(
                    "Какое то очень большое Название фильма 2",
                    R.drawable.ic_launcher_background,
                    2023,
                    "США",
                    listOf("Жанр 1", "Жанр 2", "Жанр 3", "Жанр 4", "Жанр 5", "Жанр 6"),
                    listOf(
                        Review("1", 5),
                        Review("2", 4),
                        Review("3", 3),
                        Review("4", 2),
                        Review("5", 1),
                    )
                ),                Movie(
                    "Какое то очень большое Название фильма 2",
                    R.drawable.ic_launcher_background,
                    2023,
                    "США",
                    listOf("Жанр 1", "Жанр 2", "Жанр 3", "Жанр 4", "Жанр 5", "Жанр 6"),
                    listOf(
                        Review("1", 5),
                        Review("2", 4),
                        Review("3", 3),
                        Review("4", 2),
                        Review("5", 1),
                    )
                ),                Movie(
                    "Какое то очень большое Название фильма 2",
                    R.drawable.ic_launcher_background,
                    2023,
                    "США",
                    listOf("Жанр 1", "Жанр 2", "Жанр 3", "Жанр 4", "Жанр 5", "Жанр 6"),
                    listOf(
                        Review("1", 5),
                        Review("2", 4),
                        Review("3", 3),
                        Review("4", 2),
                        Review("5", 1),
                    )
                ),
                Movie(
                    "Какое то очень большое Название фильма 2",
                    R.drawable.ic_launcher_background,
                    2023,
                    "США",
                    listOf("Жанр 1", "Жанр 2", "Жанр 3", "Жанр 4", "Жанр 5", "Жанр 6"),
                    listOf(
                        Review("1", 5),
                        Review("2", 4),
                        Review("3", 3),
                        Review("4", 2),
                        Review("5", 1),
                    )
                ),
                Movie(
                    "Какое то очень большое Название фильма 2",
                    R.drawable.ic_launcher_background,
                    2023,
                    "США",
                    listOf("Жанр 1", "Жанр 2", "Жанр 3", "Жанр 4", "Жанр 5", "Жанр 6"),
                    listOf(
                        Review("1", 5),
                        Review("2", 4),
                        Review("3", 3),
                        Review("4", 2),
                        Review("5", 1),
                    )
                ),
                Movie(
                    "Какое то очень большое Название фильма 2",
                    R.drawable.ic_launcher_background,
                    2023,
                    "США",
                    listOf("Жанр 1", "Жанр 2", "Жанр 3", "Жанр 4", "Жанр 5", "Жанр 6"),
                    listOf(
                        Review("1", 5),
                        Review("2", 4),
                        Review("3", 3),
                        Review("4", 2),
                        Review("5", 1),
                    )
                ),
                Movie(
                    "Какое то очень большое Название фильма 2",
                    R.drawable.ic_launcher_background,
                    2023,
                    "США",
                    listOf("Жанр 1", "Жанр 2", "Жанр 3", "Жанр 4", "Жанр 5", "Жанр 6"),
                    listOf(
                        Review("1", 5),
                        Review("2", 4),
                        Review("3", 3),
                        Review("4", 2),
                        Review("5", 1),
                    )
                ),
                Movie(
                    "Какое то очень большое Название фильма 2",
                    R.drawable.ic_launcher_background,
                    2023,
                    "США",
                    listOf("Жанр 1", "Жанр 2", "Жанр 3", "Жанр 4", "Жанр 5", "Жанр 6"),
                    listOf(
                        Review("1", 5),
                        Review("2", 4),
                        Review("3", 3),
                        Review("4", 2),
                        Review("5", 1),
                    )
                ),
            )
            MainScreen(navController = navController, viewModel = movieViewModel, movies = movies)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}
