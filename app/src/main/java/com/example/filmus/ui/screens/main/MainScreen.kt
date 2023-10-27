package com.example.filmus.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.filmus.navigation.AppNavigator
import com.example.filmus.navigation.BottomBar

@Composable
fun MainScreen(navController: NavController, appNavigator: AppNavigator) {
    Scaffold(bottomBar = {
        BottomBar(navController = navController, appNavigator = appNavigator)
    }, content = {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    })

}

