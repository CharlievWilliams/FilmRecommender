package com.charlievwwilliams.filmrecommender.viewstates

import com.charlievwwilliams.filmrecommender.model.movies.details.Details

data class SearchResultViewState(
    val isLoading: Boolean = true,
    val imdbID: String
)

sealed class SearchResultViewEvent {
    data class ScreenLoadingEvent(
        val id: String
    ) : SearchResultViewEvent()

    data class GetRecommendationsPressedEvent(val id: String) : SearchResultViewEvent()
}

sealed class SearchResultViewEffect {
    data class FilmSearchedEffect(
        val results: Details
    ) : SearchResultViewEffect()
}


sealed class SearchResultNavigationEffect {
    data class NavigateToRecommendationsEffect(val id: String) : SearchResultNavigationEffect()
}