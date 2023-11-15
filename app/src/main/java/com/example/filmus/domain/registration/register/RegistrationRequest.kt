package com.example.filmus.domain.registration.register

import com.squareup.moshi.Json

data class RegistrationRequest(
    @Json(name = "username") val login: String,
    @Json(name = "name") val name: String,
    @Json(name = "password") val password: String,
    @Json(name = "email") val email: String,
    @Json(name = "birthDate") val birthDate: String,
    @Json(name = "gender") val gender: Int
)