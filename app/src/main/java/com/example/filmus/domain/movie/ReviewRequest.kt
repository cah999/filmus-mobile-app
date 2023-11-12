package com.example.filmus.domain.movie

import com.squareup.moshi.Json

data class ReviewRequest(
    @Json(name = "reviewText") val reviewText: String,
    @Json(name = "rating") val rating: Int,
    @Json(name = "isAnonymous") val isAnonymous: Boolean
)