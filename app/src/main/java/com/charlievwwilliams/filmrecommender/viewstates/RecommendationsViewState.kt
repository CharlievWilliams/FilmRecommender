package com.charlievwwilliams.filmrecommender.viewstates

data class RecommendationsViewState(
    val isLoading: Boolean = true
)

sealed class RecommendationsViewEvent {
    data class ScreenLoadingEvent(
        val id: String
    ) : RecommendationsViewEvent()

}

sealed class RecommendationsViewEffect {
    data class ScreenLoadedEffect(
        val title: String
    ) : RecommendationsViewEffect()
}


sealed class RecommendationsNavigationEffect {

}