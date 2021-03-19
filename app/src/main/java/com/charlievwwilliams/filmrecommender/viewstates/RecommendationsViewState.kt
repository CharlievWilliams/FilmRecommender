package com.charlievwwilliams.filmrecommender.viewstates

import com.charlievwwilliams.filmrecommender.model.django.DjangoItem
import com.charlievwwilliams.filmrecommender.model.movies.details.Details

data class RecommendationsViewState(
    val isLoading: Boolean = true
)

sealed class RecommendationsViewEvent {
    data class ScreenLoadingEvent(
        val id: String,
        val useTitle: Boolean,
        val useGenres: Boolean,
        val useProductionCompanies: Boolean,
        val useSpokenLanguages: Boolean,
        val useKeywords: Boolean,
        val useCredits: Boolean
    ) : RecommendationsViewEvent()

}

sealed class RecommendationsViewEffect {
    data class ScreenLoadedEffect(
        val recommendedFilms: ArrayList<DjangoItem>
    ) : RecommendationsViewEffect()
}


sealed class RecommendationsNavigationEffect {

}