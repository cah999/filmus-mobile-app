package com.example.filmus.domain.movie

data class DetailedMovie(
    val id: String,
    val name: String,
    val poster: String,
    val year: Int,
    val country: String,
    val genres: List<String>,
    val reviews: List<FullReview>,
    val time: Int,
    val tagLine: String,
    val description: String,
    val director: String,
    val budget: Int,
    val fees: Int,
    val ageLimit: Int
)