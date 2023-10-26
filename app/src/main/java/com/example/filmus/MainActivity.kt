package com.example.filmus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.filmus.domain.TokenManager
import com.example.filmus.domain.registration.validation.ValidateRegistrationDataUseCase
import com.example.filmus.navigation.AppNavigation
import com.example.filmus.navigation.AppNavigator
import com.example.filmus.ui.screens.splash.LoadingScreen
import com.example.filmus.ui.theme.FilmusTheme
import com.example.filmus.viewmodel.login.LoginViewModel
import com.example.filmus.viewmodel.registration.RegistrationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val appNavigator = AppNavigator()
            val tokenManager = TokenManager(this)
            LaunchedEffect(true) {
                val token = tokenManager.getToken()
                Log.d("MainActivity", "token: ${tokenManager.getToken()}")
                if (token != null) {
                    Log.d("MainActivity", "yep token: $token")
                } else {
                    Log.d("MainActivity", "token: $token")
                }
            }
            val loginViewModel = LoginViewModel(tokenManager)
            val registrationViewModel =
                RegistrationViewModel(ValidateRegistrationDataUseCase(), tokenManager)
            FilmusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(true) {
//                        delay(2000)
                        isLoading = false
                    }
                    Crossfade(targetState = isLoading, label = "") { isLoading ->
                        if (isLoading) {
                            LoadingScreen()
                        } else {
                            AppNavigation(
                                navController,
                                appNavigator,
                                loginViewModel,
                                registrationViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    private var isLoading by mutableStateOf(true)
}
