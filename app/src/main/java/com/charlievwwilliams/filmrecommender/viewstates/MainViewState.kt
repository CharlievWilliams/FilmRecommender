package com.charlievwwilliams.filmrecommender.viewstates

data class MainViewState(
    val isLoading: Boolean = true
)

sealed class MainViewEvent {
    object ScreenLoadEvent : MainViewEvent()
    object SplashButtonPressedEvent : MainViewEvent()
}

sealed class MainNavigationEffect {
    object NavigateToCalculatorEffect : MainNavigationEffect()
}