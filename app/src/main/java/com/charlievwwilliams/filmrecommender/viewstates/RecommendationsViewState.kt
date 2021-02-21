package com.charlievwwilliams.filmrecommender.viewstates

data class RecommendationsViewState(
    val isLoading: Boolean = true
)

sealed class RecommendationsViewEvent {
    object ScreenLoadEvent : RecommendationsViewEvent()

}

sealed class RecommendationsViewEffect {

}


sealed class RecommendationsNavigationEffect {

}