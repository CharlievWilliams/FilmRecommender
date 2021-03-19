package com.charlievwwilliams.filmrecommender.model.django

data class DjangoItem(
    val id: String,
    val overview: String,
    val poster_path: String,
    val title: String
)