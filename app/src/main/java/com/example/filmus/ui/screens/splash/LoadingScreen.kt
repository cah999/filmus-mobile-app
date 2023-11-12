package com.example.filmus.ui.screens.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.filmus.R
import com.example.filmus.domain.TokenManager
import com.example.filmus.domain.api.ApiResult
import com.example.filmus.viewmodel.loading.LoadingViewModel
import com.example.filmus.viewmodel.loading.LoadingViewModelFactory

@Composable
fun LoadingScreen(navController: NavHostController, tokenManager: TokenManager) {
    val viewModel: LoadingViewModel = viewModel(factory = LoadingViewModelFactory(tokenManager))
    Log.d("LoadingScreen", "LoadingScreen: ")
    LaunchedEffect(Unit) {
        viewModel.checkUser {
            when (it) {
                is ApiResult.Success -> navController.navigate("main") {
                    popUpTo("loading") { inclusive = true }
                }

                else -> navController.navigate("welcome") {
                    popUpTo("loading") { inclusive = true }
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))

        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.5f)
                )
        )

        Image(
            painter = painterResource(id = R.drawable.logo), contentDescription = null
        )
    }
}
