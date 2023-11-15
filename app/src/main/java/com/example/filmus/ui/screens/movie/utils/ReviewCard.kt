package com.example.filmus.ui.screens.movie.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.filmus.R
import com.example.filmus.domain.movie.ReviewResponse
import com.example.filmus.ui.marks.ReviewMark
import java.text.SimpleDateFormat

@Composable
fun ReviewCard(
    review: ReviewResponse,
    isUser: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = if (review.isAnonymous) "" else review.author?.avatar ?: "",
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Success -> {
                        SubcomposeAsyncImageContent()
                    }

                    is AsyncImagePainter.State.Error -> {
                        Image(
                            painter = painterResource(id = R.drawable.anonymous),
                            contentDescription = null,
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        )
                    }

                    is AsyncImagePainter.State.Empty -> {
                        Image(
                            painter = painterResource(id = R.drawable.anonymous),
                            contentDescription = null,
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        )
                    }

                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator(
                            color = Color(0xFFFC315E), trackColor = Color(0x1AFC315E)
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                (if (review.isAnonymous) "Анонимный пользователь" else review.author?.nickName)?.let {
                    Text(
                        text = it, style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFFFFFFFF),

                            ), textAlign = TextAlign.Center
                    )
                }
                if (isUser) Text(
                    text = "мой отзыв", style = TextStyle(
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF909499),

                        )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            ReviewMark(mark = review.rating)
            var expanded by remember { mutableStateOf(false) }

            if (isUser) {
                Spacer(modifier = Modifier.width(10.dp))
                Box {
                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier
                            .background(Color(0xFF404040), shape = CircleShape)
                            .width(26.dp)
                            .height(26.dp)
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null,
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                                .graphicsLayer { rotationZ = 90f },
                            tint = Color(0xFFFFFFFF)
                        )
                    }

                    DropdownMenuNoPadding(
                        offset = DpOffset(0.dp, (8).dp),
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(
                                Color(0xFF404040), shape = RoundedCornerShape(10.dp)
                            )
                            .widthIn(177.dp),

                        ) {
                        DropdownMenuItem(
                            colors = MenuDefaults.itemColors(
                                textColor = Color(0xFFFFFFFF),
                                trailingIconColor = Color(0xFFFFFFFF),
                            ), contentPadding = PaddingValues(
                                start = 8.dp, end = 8.dp, top = 10.dp, bottom = 10.dp
                            ), trailingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.edit),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(24.dp),
                                )
                            }, text = {
                                Text(
                                    "Редактировать", style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.inter)),
                                        fontWeight = FontWeight(500),

                                        textAlign = TextAlign.Center,
                                    )
                                )

                            }, onClick = {
                                onEdit()
                                expanded = false
                            })
                        Divider(color = Color(0xFF55595D), thickness = 1.dp)
                        DropdownMenuItem(colors = MenuDefaults.itemColors(
                            textColor = Color(0xFFE64646),
                            trailingIconColor = Color(0xFFE64646),
                        ), contentPadding = PaddingValues(
                            start = 8.dp, end = 8.dp, top = 10.dp, bottom = 10.dp
                        ), trailingIcon = {
                            Icon(
                                painterResource(id = R.drawable.delete),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp),
                            )
                        }, text = {
                            Text(
                                "Удалить", style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.inter)),
                                    fontWeight = FontWeight(500),

                                    textAlign = TextAlign.Center,
                                )
                            )
                        }, onClick = {
                            onDelete()
                            expanded = false
                        })
                    }
                }
            }

        }
        review.reviewText?.let {
            Text(
                text = it, style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF),

                    ), modifier = Modifier.padding(top = 10.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormat = SimpleDateFormat("dd.MM.yyyy")
        Text(
            text = outputFormat.format(inputFormat.parse(review.createDateTime)!!),
            style = TextStyle(fontSize = 12.sp, color = Color(0xFF8C8C8C))
        )
    }
}