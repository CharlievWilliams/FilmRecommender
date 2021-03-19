package com.charlievwwilliams.filmrecommender.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.charlievwwilliams.filmrecommender.extensions.Event
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultNavigationEffect
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEffect
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewState
import com.charlievwwilliams.filmrecommender.viewstates.SplashViewEvent

class SplashViewModel : ViewModel() {

    private val viewState: MutableLiveData<SearchResultViewState> = MutableLiveData()
    private val navigationEffect = MutableLiveData<Event<SearchResultNavigationEffect>>()
    private val viewEffect = MutableLiveData<Event<SearchResultViewEffect>>()

    fun onEvent(event: SplashViewEvent) {

    }

    fun viewState(): LiveData<SearchResultViewState> = viewState
    fun getNavigationEffect(): LiveData<Event<SearchResultNavigationEffect>> = navigationEffect
    fun getViewEffect(): LiveData<Event<SearchResultViewEffect>> = viewEffect
}