package com.example.filmus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.filmus.domain.TokenManager
import com.example.filmus.navigation.AppNavigation
import com.example.filmus.navigation.BottomBar
import com.example.filmus.navigation.Screen
import com.example.filmus.navigation.TopBar
import com.example.filmus.ui.theme.FilmusTheme
import com.example.filmus.viewmodel.favorites.FavoritesViewModel
import com.example.filmus.viewmodel.favorites.FavoritesViewModelFactory
import com.example.filmus.viewmodel.login.LoginViewModel
import com.example.filmus.viewmodel.login.LoginViewModelFactory
import com.example.filmus.viewmodel.mainscreen.MainViewModel
import com.example.filmus.viewmodel.mainscreen.MainViewModelFactory
import com.example.filmus.viewmodel.movie.MovieViewModel
import com.example.filmus.viewmodel.movie.MovieViewModelFactory
import com.example.filmus.viewmodel.profile.ProfileViewModel
import com.example.filmus.viewmodel.profile.ProfileViewModelFactory
import com.example.filmus.viewmodel.registration.RegistrationViewModel
import com.example.filmus.viewmodel.registration.RegistrationViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Loading.route
            val tokenManager = TokenManager(this)
            var startScreen by remember { mutableStateOf<Screen>(Screen.Loading) }
            LaunchedEffect(true) {
                val token = tokenManager.getToken()
                startScreen = if (token != null) {
                    Screen.Main
                } else {
                    Screen.Welcome
                }
                isLoading = false
            }

            val loginViewModel: LoginViewModel by viewModels {
                LoginViewModelFactory(tokenManager)
            }
            val registrationViewModel: RegistrationViewModel by viewModels {
                RegistrationViewModelFactory(tokenManager)
            }
            val mainViewModel: MainViewModel by viewModels {
                MainViewModelFactory()
            }
            val favoritesViewModel: FavoritesViewModel by viewModels {
                FavoritesViewModelFactory()
            }
            val profileViewModel: ProfileViewModel by viewModels {
                ProfileViewModelFactory(tokenManager)
            }
            val movieViewModel: MovieViewModel by viewModels {
                MovieViewModelFactory()
            }

            FilmusTheme {
                Scaffold(topBar = {
                    when (currentRoute) {
                        Screen.Login.route, Screen.Registration.route, Screen.RegistrationPwd.route -> {
                            TopBar(navController = navController)
                        }

                        // todo null or top bar without elements??
                        else -> {}
                    }
                }, bottomBar = {
                    when (currentRoute) {
                        Screen.Main.route, Screen.Favorite.route, Screen.Profile.route -> {
                            BottomBar(navController = navController)
                        }

                        else -> {}
                    }
                }) {
                    Box(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                    ) {
                        Log.d("MainActivity", "startScreen: $startScreen")
                        AppNavigation(
                            navController = navController,
                            startScreen = startScreen,
                            loginViewModel = loginViewModel,
                            registrationViewModel = registrationViewModel,
                            mainViewModel = mainViewModel,
                            favoritesViewModel = favoritesViewModel,
                            profileViewModel = profileViewModel,
                            movieViewModel = movieViewModel
                        )
                    }
                }
            }
        }
    }

    private var isLoading by mutableStateOf(true)
}
