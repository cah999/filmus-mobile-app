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
import com.example.filmus.ui.screens.main.Author
import com.example.filmus.ui.screens.main.DetailedMovie
import com.example.filmus.ui.screens.main.MainScreen
import com.example.filmus.ui.screens.main.MovieDetailsScreen
import com.example.filmus.ui.screens.main.Review
import com.example.filmus.ui.screens.profile.ProfileScreen
import com.example.filmus.ui.screens.registration.RegistrationPwdScreen
import com.example.filmus.ui.screens.registration.RegistrationScreen
import com.example.filmus.ui.screens.splash.LoadingScreen
import com.example.filmus.ui.screens.welcome.WelcomeScreen
import com.example.filmus.viewmodel.login.LoginViewModel
import com.example.filmus.viewmodel.registration.RegistrationViewModel
import java.util.Date

@Composable
fun AppNavigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registrationViewModel: RegistrationViewModel,
    startScreen: Screen
) {
    NavHost(
        navController,
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
            MainScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.Movie.route) {
            val author = Author(
                userId = "user123",
                nickname = "MovieBuff",
                avatar = "https://avatar-url.com/avatar.png"
            )

            val review = Review(
                id = "review456",
                rating = 6,
                reviewText = "A fantastic movie! Loved it!",
                isAnonymous = false,
                createDateTime = Date(),
                author = author
            )

            val movie = DetailedMovie(
                name = "Inception",
                poster = "https://poster-url.com/inception.png",
                year = 2010,
                country = "USA",
                genres = listOf("Action", "Adventure", "Sci-Fi"),
                reviews = listOf(review, review, review, review, review, review),
                time = 148,
                tagLine = "The dream is real",
                description = "Inception is a 2010 science fiction action film some long long long long long long long long long lnoglnognglnglnlgnolgafsaljkfhjblsafkjasfkLGJGKLFKGLDJSGFKHJLASDGKJGHKJSDAHKJLGSDAHKJLGSDALK;HJGSHLDJA;GLH;SADGLSDLAGHJL",
                director = "Christopher Nolan",
                budget = 160_000_000,
                fees = 828_322_032,
                ageLimit = 13
            )
            MovieDetailsScreen(movie = movie, isFavorite = false, onFavoriteToggle = { })
        }
    }
}
