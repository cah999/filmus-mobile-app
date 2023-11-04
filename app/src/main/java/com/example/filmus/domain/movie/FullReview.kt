package com.example.filmus.domain.movie

import java.util.Date

data class FullReview(
    val id: String,
    val rating: Int,
    val reviewText: String,
    val isAnonymous: Boolean,
    val createDateTime: Date,
    val author: Author
)