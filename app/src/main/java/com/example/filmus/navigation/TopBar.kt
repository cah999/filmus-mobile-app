package com.example.filmus.navigation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.filmus.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController, logo: Boolean = true, back: Boolean = true) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFF1D1D1D),
            navigationIconContentColor = Color(0xFFFFFFFF),
        ),
        title = {
            if (logo) {
                Image(
                    painter = painterResource(id = R.drawable.bar_logo), contentDescription = null
                )
            }
        }, navigationIcon = {
            if (back) {
                IconButton(onClick = {
                    Log.d("TopBar", "$navController")
                    Log.d("TopBar", "${navController.currentBackStackEntry}")
                    navController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back), contentDescription = null
                    )
                }
            }
        })
}