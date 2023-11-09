package com.example.filmus.ui.screens.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.filmus.ui.screens.main.customShimmer

@Composable
fun MovieDetailsPlaceholder() {
    Box(
        modifier = Modifier
            .width(360.dp)
            .height(497.dp)
            .customShimmer(1000)
            .background(Color.Gray),
    )
    Spacer(modifier = Modifier.height(16.dp))
    Column(Modifier.padding(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(51.dp)
                    .height(26.dp)
                    .customShimmer(1000)
                    .background(Color.Gray),
            )
            Box(
                modifier = Modifier
                    .width(212.dp)
                    .customShimmer(1000)
                    .background(Color.Gray),
            )
            Box(
                modifier = Modifier
                    .background(Color(0xFF404040), shape = CircleShape)
                    .width(40.dp)
                    .height(40.dp)
                    .customShimmer(1000)
                    .background(Color.Gray),
            )

        }
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .customShimmer(1000)
                .background(Color.Gray),
        )
    }
}