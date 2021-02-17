package com.charlievwwilliams.filmrecommender.viewstates

import com.charlievwwilliams.filmrecommender.model.search.Search

data class MainViewState(
    val isLoading: Boolean = true
)

sealed class MainViewEvent {
    object ScreenLoadEvent : MainViewEvent()
    object SplashButtonPressedEvent : MainViewEvent()
}

sealed class MainViewEffect {
    data class FilmSearchedEffect(
        val results: Search
    ) : MainViewEffect()
}


sealed class MainNavigationEffect {
    object NavigateToCalculatorEffect : MainNavigationEffect()
}