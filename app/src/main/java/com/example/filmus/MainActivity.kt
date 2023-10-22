package com.example.filmus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.example.filmus.navigation.AppNavigation
import com.example.filmus.navigation.AppNavigator
import com.example.filmus.ui.screens.LoadingScreen
import com.example.filmus.ui.theme.FilmusTheme
import com.example.filmus.viewmodel.login.LoginViewModel
import com.example.filmus.viewmodel.registration.RegistrationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val appNavigator = AppNavigator()
            val loginViewModel: LoginViewModel by viewModels()
            val registrationViewModel: RegistrationViewModel by viewModels()
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
