package com.example.filmus.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class AppNavigator(startScreen: Screen = Screen.Welcome) {
    val currentScreen: MutableState<Screen> = mutableStateOf(startScreen)
}