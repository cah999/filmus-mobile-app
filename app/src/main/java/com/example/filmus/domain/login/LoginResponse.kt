package com.example.filmus.domain.login

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "token") val token: String
)