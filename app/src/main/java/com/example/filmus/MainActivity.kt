package com.example.filmus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.filmus.api.ApiService
import com.example.filmus.domain.model.LoginUseCase
import com.example.filmus.navigation.AppNavigation
import com.example.filmus.navigation.AppNavigator
import com.example.filmus.repository.LoginRepository
import com.example.filmus.ui.screens.LoadingScreen
import com.example.filmus.ui.screens.LoginScreen
import com.example.filmus.ui.screens.WelcomeScreen
import com.example.filmus.ui.theme.FilmusTheme
import com.example.filmus.viewmodel.login.LoginViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val appNavigator = AppNavigator()
            val loginViewModel: LoginViewModel by viewModels()
            FilmusTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(true) {
                        delay(2000)
                        isLoading = false
                    }
                    Crossfade(targetState = isLoading, label = "") { isLoading ->
                        if (isLoading) {
                            LoadingScreen()
                        } else {
                            AppNavigation(navController, appNavigator, loginViewModel)
                        }
                    }
                }
            }
        }
    }

    private var isLoading by mutableStateOf(true)
}
