package com.example.filmus.ui.screens.main.carousel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotsIndicator(
    pagerState: PagerState, itemCount: Int, modifier: Modifier = Modifier
) {
    val displayItemCount = if (itemCount > 4) 4 else itemCount

    Box(
        modifier = modifier
            .width(72.dp)
            .height(24.dp)
            .background(Color(0x1AFFFFFF).copy(alpha = 0.1f), RoundedCornerShape(28.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            repeat(displayItemCount) { i ->
                val isSelected = pagerState.currentPage == i
                val color = if (isSelected) Color.White else Color.Transparent
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color = color, shape = CircleShape)
                        .border(1.dp, Color.White, shape = CircleShape)
                )
            }
        }
    }
}