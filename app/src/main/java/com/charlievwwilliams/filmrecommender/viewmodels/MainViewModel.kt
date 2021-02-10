package com.charlievwwilliams.filmrecommender.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.charlievwwilliams.filmrecommender.extensions.Event
import com.charlievwwilliams.filmrecommender.viewstates.MainNavigationEffect
import com.charlievwwilliams.filmrecommender.viewstates.MainNavigationEffect.*
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEvent
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEvent.*
import com.charlievwwilliams.filmrecommender.viewstates.MainViewState

class MainViewModel : ViewModel() {

    private val viewState: MutableLiveData<MainViewState> = MutableLiveData()
    private val navigationEffect = MutableLiveData<Event<MainNavigationEffect>>()

    fun onEvent(event: MainViewEvent) {
        when (event) {
            is ScreenLoadEvent -> onScreenLoad()
            is SplashButtonPressedEvent -> navigationEffect.value =
                Event(NavigateToCalculatorEffect)
        }
    }

    private fun onScreenLoad() {
        onScreenLoadSuccess()
    }

    private fun onScreenLoadSuccess() {
        viewState.value = MainViewState(
            isLoading = false
        )
    }

    fun viewState(): LiveData<MainViewState> = viewState
    fun getNavigationEffect(): LiveData<Event<MainNavigationEffect>> = navigationEffect
}