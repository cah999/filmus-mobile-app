package com.example.filmus.domain.registration.register

import com.squareup.moshi.Json

data class RegistrationResponse(
    @Json(name = "token") val token: String
)