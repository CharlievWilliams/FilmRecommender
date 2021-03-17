package com.charlievwwilliams.filmrecommender.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.charlievwwilliams.filmrecommender.api.RetrofitInstance
import com.charlievwwilliams.filmrecommender.extensions.Event
import com.charlievwwilliams.filmrecommender.model.django.Django
import com.charlievwwilliams.filmrecommender.model.movies.details.Details
import com.charlievwwilliams.filmrecommender.utils.Constants
import com.charlievwwilliams.filmrecommender.viewstates.*
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEffect.ScreenLoadedEffect
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEvent.ScreenLoadingEvent
import kotlinx.coroutines.*

class RecommendationsViewModel : ViewModel() {

    private val viewState: MutableLiveData<RecommendationsViewState> = MutableLiveData()
    private val navigationEffect = MutableLiveData<Event<RecommendationsNavigationEffect>>()
    private val viewEffect = MutableLiveData<Event<RecommendationsViewEffect>>()

    fun onEvent(event: RecommendationsViewEvent) {
        when (event) {
            is ScreenLoadingEvent -> retrieveRecommendations(event.id)
        }
    }

    private fun retrieveRecommendations(input: String) {
        viewState.value = RecommendationsViewState(isLoading = true)
        CoroutineScope(Dispatchers.IO).launch {
            val result = getResultFromDjango(input)
            val recommendedFilms = mutableListOf<Details>()
            // TODO: Pass back film details through Django for fewer API requests
            for (item in result.id) {
                recommendedFilms.add(getResultFromAPI(item))
            }
            withContext(Dispatchers.Main) {
                viewState.value = RecommendationsViewState(isLoading = false)
                viewEffect.value = Event(ScreenLoadedEffect(recommendedFilms[2].title)) // TODO: Do something with these
            }
        }
    }

    private suspend fun getResultFromDjango(input: String): Django {
        delay(500)
        return RetrofitInstance.djangoApi.getRecommendations(input)
    }

    private suspend fun getResultFromAPI(input: String): Details {
        delay(500)
        return RetrofitInstance.filmApi.getMovieDetails(input, Constants.API_KEY, "en-US")
    }

    fun viewState(): LiveData<RecommendationsViewState> = viewState
    fun getNavigationEffect(): LiveData<Event<RecommendationsNavigationEffect>> = navigationEffect
    fun getViewEffect(): LiveData<Event<RecommendationsViewEffect>> = viewEffect
}