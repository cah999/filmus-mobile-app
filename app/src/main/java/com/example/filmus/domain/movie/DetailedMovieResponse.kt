package com.example.filmus.domain.movie

import com.squareup.moshi.Json

data class DetailedMovieResponse(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String?,
    @Json(name = "poster") val poster: String?,
    @Json(name = "year") val year: Int,
    @Json(name = "country") val country: String?,
    @Json(name = "genres") val genres: List<GenreResponse?>,
    @Json(name = "reviews") val reviews: List<ReviewResponse?>,
    @Json(name = "time") val time: Int,
    @Json(name = "tagline") val tagline: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "director") val director: String?,
    @Json(name = "budget") val budget: Int?,
    @Json(name = "fees") val fees: Int?,
    @Json(name = "ageLimit") val ageLimit: Int
)
