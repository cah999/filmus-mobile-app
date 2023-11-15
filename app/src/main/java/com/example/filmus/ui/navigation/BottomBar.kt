package com.example.filmus.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.filmus.R

@Composable
fun BottomBar(navController: NavController) {
    Column {
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xA6545458),
            thickness = 1.dp
        )
        BottomNavigation(
            backgroundColor = Color(0xFF161616),
            modifier = Modifier
                .width(360.dp)
                .height(67.dp)
        ) {
            val screensToDisplay = listOf(Screen.Main, Screen.Favorite, Screen.Profile)
            val currentScreen = navController.currentBackStackEntry?.destination?.route

            screensToDisplay.forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        screen.imageResource?.let { resource ->
                            Icon(
                                painter = painterResource(resource),
                                contentDescription = null
                            )
                        }
                    },
                    label = {
                        Text(
                            text = screen.title,
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(400),
                                textAlign = TextAlign.Center,
                            )
                        )
                    },
                    selected = currentScreen == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    selectedContentColor = Color(0xFFFC315E),
                    unselectedContentColor = Color(0xFF909499),
                )
            }
        }
    }
}
