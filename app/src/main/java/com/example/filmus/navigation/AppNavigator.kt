package com.example.filmus.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class AppNavigator {
    val currentScreen: MutableState<Screen> = mutableStateOf(Screen.Welcome)
}