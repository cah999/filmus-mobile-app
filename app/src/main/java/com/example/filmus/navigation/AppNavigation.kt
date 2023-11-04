package com.example.filmus.navigation

import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.filmus.R
import com.example.filmus.domain.favorite.Poster
import com.example.filmus.domain.main.Movie
import com.example.filmus.domain.main.Review
import com.example.filmus.domain.movie.Author
import com.example.filmus.domain.movie.DetailedMovie
import com.example.filmus.domain.movie.FullReview
import com.example.filmus.ui.screens.favorites.FavoritesScreen
import com.example.filmus.ui.screens.login.LoginScreen
import com.example.filmus.ui.screens.main.MainScreen
import com.example.filmus.ui.screens.movie.MovieDetailsScreen

import com.example.filmus.ui.screens.profile.ProfileScreen
import com.example.filmus.ui.screens.registration.RegistrationPwdScreen
import com.example.filmus.ui.screens.registration.RegistrationScreen
import com.example.filmus.ui.screens.splash.LoadingScreen
import com.example.filmus.ui.screens.welcome.WelcomeScreen
import com.example.filmus.viewmodel.favorites.FavoritesViewModel
import com.example.filmus.viewmodel.login.LoginViewModel
import com.example.filmus.viewmodel.mainscreen.MainViewModel
import com.example.filmus.viewmodel.movie.MovieViewModel
import com.example.filmus.viewmodel.profile.ProfileViewModel
import com.example.filmus.viewmodel.registration.RegistrationViewModel
import java.util.Date

@Composable
fun AppNavigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registrationViewModel: RegistrationViewModel,
    mainViewModel: MainViewModel,
    movieViewModel: MovieViewModel,
    favoritesViewModel: FavoritesViewModel,
    profileViewModel: ProfileViewModel,

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
            FavoritesScreen(
                posters = posters, navController = navController, viewModel = favoritesViewModel
            )
        }
        composable(Screen.Main.route) {
            val movies = listOf(
                Movie(
                    "1",
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
                    "2",
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
                    "3",
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
                    "4",
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
                    "5",
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
                    "6",
                    "Название",
                    R.drawable.ic_launcher_background,
                    2023,
                    "США",
                    listOf("Жанр 1", "Жанр 2", "Жанр 3", "Жанр 4", "Жанр 5", "Жанр 6"),
                    listOf(
                        Review("1", 1),
                        Review("2", 1),
                        Review("3", 1),
                        Review("4", 1),
                        Review("5", 1),
                    )
                ),
            )
            MainScreen(navController = navController, viewModel = mainViewModel, movies = movies)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController, viewModel = profileViewModel)
        }
        composable(
            "${Screen.Movie.route}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
            Log.d("TAG", "AppNavigation: $movieId")
            val author = Author(
                userId = "user123",
                nickname = "MovieBuff",
                avatar = "https://avatar-url.com/avatar.png"
            )
            val review = FullReview(
                id = "review456",
                rating = 6,
                reviewText = "A fantastic movie! Loved it!",
                isAnonymous = false,
                createDateTime = Date(),
                author = author
            )
            val review2 = FullReview(
                id = "review4536",
                rating = 9,
                reviewText = "NO way I LOVE IT SOSOSOSOSOSOSOSOOSOSOOSOSOS MUCH",
                isAnonymous = true,
                createDateTime = Date(),
                author = author
            )
            val movie = DetailedMovie(
                id = "1",
                name = "Inception",
                poster = "https://poster-url.com/inception.png",
                year = 2010,
                country = "USA",
                genres = listOf("Action", "Adventure", "Sci-Fi"),
                reviews = listOf(review, review2),
                time = 148,
                tagLine = "The dream is real",
                description = "Inception is a 2010 science fiction action film some long long long long long long long long long lnoglnognglnglnlgnolgafsaljkfhjblsafkjasfkLGJGKLFKGLDJSGFKHJLASDGKJGHKJSDAHKJLGSDAHKJLGSDALK;HJGSHLDJA;GLH;SADGLSDLAGHJL",
                director = "Christopher Nolan",
                budget = 160_000_000,
                fees = 828_322_032,
                ageLimit = 13
            )
            MovieDetailsScreen(
                movie = movie,
                isFavorite = false,
                onFavoriteToggle = { },
                navController = navController,
                viewModel = movieViewModel
            )
        }
    }
}
