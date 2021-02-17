package com.charlievwwilliams.filmrecommender.model.movies.credits

data class Credits(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)