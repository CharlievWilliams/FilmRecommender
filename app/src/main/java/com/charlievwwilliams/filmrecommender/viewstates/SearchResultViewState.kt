package com.charlievwwilliams.filmrecommender.viewstates

import com.charlievwwilliams.filmrecommender.model.search.Search

data class SearchResultViewState(
    val isLoading: Boolean = true
)

sealed class SearchResultViewEvent {
    object ScreenLoadEvent : SearchResultViewEvent()
}

sealed class SearchResultViewEffect {
    data class FilmSearchedEffect(
        val results: Search
    ) : SearchResultViewEffect()
}


sealed class SearchResultNavigationEffect {
    object NavigateToRecommendationsEffect : SearchResultNavigationEffect()
}