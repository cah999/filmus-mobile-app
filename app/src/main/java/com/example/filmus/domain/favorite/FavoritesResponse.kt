package com.example.filmus.domain.favorite

import com.example.filmus.domain.main.Movie
import com.squareup.moshi.Json

data class FavoritesResponse(
    @Json(name = "movies") val results: List<Movie>
)