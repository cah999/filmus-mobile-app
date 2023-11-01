package com.example.filmus.domain.favorite

data class Poster(
    val imageResource: Int, val title: String, val rating: Int? = null
)