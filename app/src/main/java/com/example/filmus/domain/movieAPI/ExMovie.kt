package com.example.filmus.domain.movieAPI

data class ExMovie(
    val id: Int,
    val translations: List<Translation>,
    val isSerial: Boolean,
)