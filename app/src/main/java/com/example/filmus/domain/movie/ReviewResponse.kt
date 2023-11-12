package com.example.filmus.domain.movie

import com.squareup.moshi.Json

data class ReviewResponse(
    @Json(name = "id") val id: String,
    @Json(name = "rating") val rating: Int,
    @Json(name = "reviewText") val reviewText: String?,
    @Json(name = "isAnonymous") val isAnonymous: Boolean,
    @Json(name = "createDateTime") val createDateTime: String,
    @Json(name = "author") val author: AuthorResponse?
)