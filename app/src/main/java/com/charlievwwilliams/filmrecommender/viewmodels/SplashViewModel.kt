package com.charlievwwilliams.filmrecommender.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.charlievwwilliams.filmrecommender.extensions.Event
import com.charlievwwilliams.filmrecommender.viewstates.SplashNavigationEffect
import com.charlievwwilliams.filmrecommender.viewstates.SplashViewEvent
import com.charlievwwilliams.filmrecommender.viewstates.SplashViewEvent.AnimationCompleteEvent

class SplashViewModel : ViewModel() {

    private val navigationEffect = MutableLiveData<Event<SplashNavigationEffect>>()

    fun onEvent(event: SplashViewEvent) {
        when (event) {
            is AnimationCompleteEvent -> navigationEffect.value =
                Event(SplashNavigationEffect.NavigateToAppEffect)
        }
    }

    fun getNavigationEffect(): LiveData<Event<SplashNavigationEffect>> = navigationEffect
}