package com.example.filmus

import android.os.Bundle
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
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.filmus.domain.UserManager
import com.example.filmus.navigation.AppNavigation
import com.example.filmus.navigation.BottomBar
import com.example.filmus.navigation.Screen
import com.example.filmus.navigation.TopBar
import com.example.filmus.ui.theme.FilmusTheme
import com.example.filmus.viewmodel.registration.RegistrationViewModel
import com.example.filmus.viewmodel.registration.RegistrationViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Loading.route
            val userManager = UserManager(this)
            var startScreen by remember { mutableStateOf<Screen>(Screen.Loading) }
            LaunchedEffect(true) {
                startScreen = if (userManager.checkToken()) {
                    Screen.Main
                } else {
                    Screen.Welcome
                }
                isLoading = false
            }
            val registrationViewModel: RegistrationViewModel by viewModels {
                RegistrationViewModelFactory(userManager)
            }

            FilmusTheme {
                Scaffold(
                    containerColor = Color(0xFF1D1D1D),
                    topBar = {
                        when (currentRoute) {
                            Screen.Login.route, Screen.Registration.route, Screen.RegistrationPwd.route -> {
                                TopBar(navController = navController)
                            }

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
                        AppNavigation(
                            navController = navController,
                            startScreen = startScreen,
                            userManager = userManager,
                            registrationViewModel = registrationViewModel,
                        )
                    }
                }
            }
        }
    }

    private var isLoading by mutableStateOf(true)
}
