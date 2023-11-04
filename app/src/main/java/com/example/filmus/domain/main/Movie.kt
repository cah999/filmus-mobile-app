package com.example.filmus.domain.main

data class Movie(
    val id: String,
    val name: String,
    val poster: Int,
    val year: Int,
    val country: String,
    val genres: List<String>,
    val reviews: List<Review>
)