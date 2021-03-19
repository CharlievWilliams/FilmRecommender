package com.charlievwwilliams.filmrecommender.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.charlievwwilliams.filmrecommender.api.RetrofitInstance
import com.charlievwwilliams.filmrecommender.extensions.Event
import com.charlievwwilliams.filmrecommender.model.django.Django
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsNavigationEffect
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEffect
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEffect.ScreenLoadedEffect
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEvent
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEvent.ScreenLoadingEvent
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewState
import kotlinx.coroutines.*

class RecommendationsViewModel : ViewModel() {

    private val viewState: MutableLiveData<RecommendationsViewState> = MutableLiveData()
    private val navigationEffect = MutableLiveData<Event<RecommendationsNavigationEffect>>()
    private val viewEffect = MutableLiveData<Event<RecommendationsViewEffect>>()

    fun onEvent(event: RecommendationsViewEvent) {
        when (event) {
            is ScreenLoadingEvent -> retrieveRecommendations(
                event.id,
                event.useTitle,
                event.useGenres,
                event.useProductionCompanies,
                event.useSpokenLanguages,
                event.useKeywords,
                event.useCredits
            )
        }
    }

    private fun retrieveRecommendations(
        id: String,
        useTitle: Boolean,
        useGenres: Boolean,
        useProductionCompanies: Boolean,
        useSpokenLanguages: Boolean,
        useKeywords: Boolean,
        useCredits: Boolean
    ) {
        viewState.value = RecommendationsViewState(isLoading = true)
        CoroutineScope(Dispatchers.IO).launch {
            val result = getResultFromDjango(
                id,
                useTitle,
                useGenres,
                useProductionCompanies,
                useSpokenLanguages,
                useKeywords,
                useCredits
            )
            withContext(Dispatchers.Main) {
                viewState.value = RecommendationsViewState(isLoading = false)
                viewEffect.value =
                    Event(ScreenLoadedEffect(result)) // TODO: Do something with these
            }
        }
    }

    private suspend fun getResultFromDjango(
        id: String,
        useTitle: Boolean,
        useGenres: Boolean,
        useProductionCompanies: Boolean,
        useSpokenLanguages: Boolean,
        useKeywords: Boolean,
        useCredits: Boolean
    ): Django {
        delay(500)
        return RetrofitInstance.djangoApi.getRecommendations(
            id,
            useTitle,
            useGenres,
            useProductionCompanies,
            useSpokenLanguages,
            useKeywords,
            useCredits
        )
    }

    fun viewState(): LiveData<RecommendationsViewState> = viewState
    fun getNavigationEffect(): LiveData<Event<RecommendationsNavigationEffect>> = navigationEffect
    fun getViewEffect(): LiveData<Event<RecommendationsViewEffect>> = viewEffect
}