package com.charlievwwilliams.filmrecommender.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.charlievwwilliams.filmrecommender.api.RetrofitInstance
import com.charlievwwilliams.filmrecommender.extensions.Event
import com.charlievwwilliams.filmrecommender.model.search.Search
import com.charlievwwilliams.filmrecommender.utils.Constants.Companion.API_KEY
import com.charlievwwilliams.filmrecommender.viewstates.*
import com.charlievwwilliams.filmrecommender.viewstates.MainNavigationEffect.NavigateToResultEffect
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEffect.FilmSearchedEffect
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEffect.OpenCameraEffect
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEvent.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val viewState: MutableLiveData<MainViewState> = MutableLiveData()
    private val navigationEffect = MutableLiveData<Event<MainNavigationEffect>>()
    private val viewEffect = MutableLiveData<Event<MainViewEffect>>()

    fun onEvent(event: MainViewEvent) {
        when (event) {
            is ScreenLoadEvent -> onScreenLoad()
            is TakePicturePressedEvent -> viewEffect.value = Event(OpenCameraEffect)
            is SubmitPressedEvent -> searchFilm(event.title)
            is ItemPressedEvent -> navigationEffect.value = Event(NavigateToResultEffect(event.id))
        }
    }

    private fun onScreenLoad() {
        viewState.value = MainViewState(isLoading = false)
    }

    private fun searchFilm(input: String) {
        viewState.value = MainViewState(isLoading = true)
        CoroutineScope(IO).launch {
            val result = getResultFromAPI(input)
            withContext(Main) {
                viewState.value = MainViewState(isLoading = false)
                viewEffect.value = Event(FilmSearchedEffect(result))
            }
        }
    }

    private suspend fun getResultFromAPI(input: String): Search {
        delay(500)
        return RetrofitInstance.filmApi.searchMovies(API_KEY, "en-US", input, "1", "true")
    }

    fun viewState(): LiveData<MainViewState> = viewState
    fun getNavigationEffect(): LiveData<Event<MainNavigationEffect>> = navigationEffect
    fun getViewEffect(): LiveData<Event<MainViewEffect>> = viewEffect
}