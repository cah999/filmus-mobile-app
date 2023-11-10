package com.example.filmus.ui.marks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.example.filmus.R

@Composable
fun ReviewMark(mark: Int, modifier: Modifier = Modifier) {
    val containerColor = remember {
        when {
            (mark in 0..2) -> 0xFFE64646
            (mark in 3..3) -> 0xFFF05C44
            (mark in 4..5) -> 0xFFFFA000
            (mark in 6..7) -> 0xFFFFD54F
            (mark in 8..8) -> 0xFFA3CD4A
            (mark >= 9) -> 0xFF4BB34B
            else -> 0xFFFFFFFF

        }
    }

    val text = mark.toString()

    Box(
        modifier = modifier
            .background(
                color = Color(containerColor),
                shape = RoundedCornerShape(size = 35.dp)
            )
            .width(if (mark >= 10) 41.dp else 38.dp)
            .height(26.dp)
            .padding(start = 4.dp, end = 0.dp)
            .wrapContentSize(align = Alignment.Center),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.star_3),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = Color(0xFFFFFFFF)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }


}
