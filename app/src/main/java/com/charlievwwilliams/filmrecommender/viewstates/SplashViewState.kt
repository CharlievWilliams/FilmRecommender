package com.charlievwwilliams.filmrecommender.viewstates

sealed class SplashViewEvent {
    object AnimationCompleteEvent : SplashViewEvent()
}

sealed class SplashNavigationEffect {
    object NavigateToAppEffect : SplashNavigationEffect()
}