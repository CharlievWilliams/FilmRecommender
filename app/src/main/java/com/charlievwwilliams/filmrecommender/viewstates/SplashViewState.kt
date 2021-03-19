package com.charlievwwilliams.filmrecommender.viewstates

data class SplashViewState(
    val isLoading: Boolean = false
)

sealed class SplashViewEvent {
    object ScreenLoadEvent : SplashViewEvent()
}

sealed class SplashViewEffect {
    object FilmSearchedEffect : SplashViewEffect()
}

sealed class SplashNavigationEffect {
    object NavigateToResultEffect : SplashNavigationEffect()
}