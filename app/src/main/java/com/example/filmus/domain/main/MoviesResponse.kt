package com.example.filmus.domain.main

import com.squareup.moshi.Json

data class MoviesResponse(
    @Json(name = "movies") val results: List<Movie>
)