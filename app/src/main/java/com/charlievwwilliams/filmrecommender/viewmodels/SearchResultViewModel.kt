package com.charlievwwilliams.filmrecommender.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.charlievwwilliams.filmrecommender.api.RetrofitInstance
import com.charlievwwilliams.filmrecommender.extensions.Event
import com.charlievwwilliams.filmrecommender.model.movies.details.Details
import com.charlievwwilliams.filmrecommender.utils.Constants
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultNavigationEffect
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultNavigationEffect.NavigateToRecommendationsEffect
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEffect
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEffect.FilmSearchedEffect
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEvent
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEvent.GetRecommendationsPressedEvent
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEvent.ScreenLoadingEvent
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewState
import kotlinx.coroutines.*

class SearchResultViewModel : ViewModel() {

    private val viewState: MutableLiveData<SearchResultViewState> = MutableLiveData()
    private val navigationEffect = MutableLiveData<Event<SearchResultNavigationEffect>>()
    private val viewEffect = MutableLiveData<Event<SearchResultViewEffect>>()

    fun onEvent(event: SearchResultViewEvent) {
        when (event) {
            is ScreenLoadingEvent -> searchFilm(event.id)
            is GetRecommendationsPressedEvent -> navigationEffect.value =
                Event(NavigateToRecommendationsEffect(event.id))
        }
    }

    private fun searchFilm(input: String) {
        viewState.value = SearchResultViewState(isLoading = true, "")
        CoroutineScope(Dispatchers.IO).launch {
            val result = getResultFromAPI(input)
            withContext(Dispatchers.Main) {
                viewState.value = SearchResultViewState(isLoading = false, result.id.toString())
                viewEffect.value = Event(FilmSearchedEffect(result))
            }
        }
    }

    private suspend fun getResultFromAPI(input: String): Details {
        delay(500)
        return RetrofitInstance.filmApi.getMovieDetails(input, Constants.API_KEY, "en-US")
    }

    fun viewState(): LiveData<SearchResultViewState> = viewState
    fun getNavigationEffect(): LiveData<Event<SearchResultNavigationEffect>> = navigationEffect
    fun getViewEffect(): LiveData<Event<SearchResultViewEffect>> = viewEffect
}