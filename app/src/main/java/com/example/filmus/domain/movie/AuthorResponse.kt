package com.example.filmus.domain.movie

import com.squareup.moshi.Json

data class AuthorResponse(
    @Json(name = "userId") val userId: String,
    @Json(name = "nickName") val nickName: String?,
    @Json(name = "avatar") val avatar: String?
)