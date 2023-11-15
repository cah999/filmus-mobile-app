package com.example.filmus.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.filmus.ui.screens.main.utils.customShimmer

@Composable
fun ProfilePlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(Color.Gray, shape = CircleShape)
                    .customShimmer(1000)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))


        Box(
            Modifier
                .height(42.dp)
                .fillMaxWidth()
                .background(Color.Gray)
                .customShimmer(1000)
        )
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            Modifier
                .height(42.dp)
                .fillMaxWidth()
                .background(Color.Gray)
                .customShimmer(1000)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            Modifier
                .height(42.dp)
                .fillMaxWidth()
                .background(Color.Gray)
                .customShimmer(1000)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            Modifier
                .height(42.dp)
                .fillMaxWidth()
                .background(Color.Gray)
                .customShimmer(1000)
        )
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            Modifier
                .height(42.dp)
                .fillMaxWidth()
                .background(Color.Gray)
                .customShimmer(1000)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            Modifier
                .height(42.dp)
                .fillMaxWidth()
                .background(Color.Gray)
                .customShimmer(1000)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            Modifier
                .height(42.dp)
                .fillMaxWidth()
                .background(Color.Gray)
                .customShimmer(1000)
        )

    }
}