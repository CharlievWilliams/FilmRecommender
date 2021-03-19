package com.charlievwwilliams.filmrecommender.viewstates

import com.charlievwwilliams.filmrecommender.model.search.Search

data class MainViewState(
    val isLoading: Boolean = false
)

sealed class MainViewEvent {
    object ScreenLoadEvent : MainViewEvent()
    object TakePicturePressedEvent : MainViewEvent()
    data class SubmitPressedEvent(val title: String) : MainViewEvent()
    data class ItemPressedEvent(val id: String) : MainViewEvent()
}

sealed class MainViewEffect {
    object OpenCameraEffect : MainViewEffect()
    data class FilmSearchedEffect(val results: Search) : MainViewEffect()
}


sealed class MainNavigationEffect {
    data class NavigateToResultEffect(val id: String) : MainNavigationEffect()
}