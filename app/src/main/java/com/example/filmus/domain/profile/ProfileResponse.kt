package com.example.filmus.domain.profile

import com.squareup.moshi.Json

data class ProfileResponse(
    @Json(name = "id") val id: String,
    @Json(name = "nickName") val nickname: String,
    @Json(name = "email") val email: String,
    @Json(name = "avatarLink") val avatarLink: String?,
    @Json(name = "name") val name: String,
    @Json(name = "birthDate") val birthDate: String,
    @Json(name = "gender") val gender: Int
)