package com.example.filmus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.filmus.domain.network.ConnectionState
import com.example.filmus.repository.TokenManager
import com.example.filmus.repository.network.connectivityState
import com.example.filmus.ui.navigation.AppNavigation
import com.example.filmus.ui.navigation.BottomBar
import com.example.filmus.ui.navigation.Screen
import com.example.filmus.ui.navigation.TopBar
import com.example.filmus.ui.screens.network.NoConnectionScreen
import com.example.filmus.ui.theme.FilmusTheme
import com.example.filmus.viewmodel.registration.RegistrationViewModel
import com.example.filmus.viewmodel.registration.RegistrationViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Loading.route
            val tokenManager = TokenManager(this)
            val connection by connectivityState()
            val isConnected = connection === ConnectionState.Available
            val registrationViewModel: RegistrationViewModel by viewModels(
                factoryProducer = {
                    RegistrationViewModelFactory(tokenManager)
                }
            )

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
                        if (!isConnected) {
                            NoConnectionScreen()
                        } else {
                            AppNavigation(
                                navController = navController,
                                registrationViewModel = registrationViewModel,
                                tokenManager = tokenManager,
                            )
                        }
                    }
                }
            }
        }
    }
}
