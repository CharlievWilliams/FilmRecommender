package com.charlievwwilliams.filmrecommender.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.charlievwwilliams.filmrecommender.api.RetrofitInstance
import com.charlievwwilliams.filmrecommender.extensions.Event
import com.charlievwwilliams.filmrecommender.model.movies.details.Details
import com.charlievwwilliams.filmrecommender.utils.Constants
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsNavigationEffect
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEffect
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewState
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEvent
import kotlinx.coroutines.*

class RecommendationsViewModel : ViewModel() {

    private val viewState: MutableLiveData<RecommendationsViewState> = MutableLiveData()
    private val navigationEffect = MutableLiveData<Event<RecommendationsNavigationEffect>>()
    private val viewEffect = MutableLiveData<Event<RecommendationsViewEffect>>()

    fun onEvent(event: SearchResultViewEvent) {
        when (event) {

        }
    }

    private fun onScreenLoad() {
        viewState.value = RecommendationsViewState(isLoading = false)
    }

    private fun searchFilm(input: String) {
        viewState.value = RecommendationsViewState(isLoading = true)
        CoroutineScope(Dispatchers.IO).launch {
            val result = getResultFromAPI(input)
            withContext(Dispatchers.Main) {
                viewState.value = RecommendationsViewState(isLoading = false)
            }
        }
    }

    private suspend fun getResultFromAPI(input: String): Details {
        delay(500)
        return RetrofitInstance.Api.getMovieDetails(input, Constants.API_KEY, "en-US")
    }

    fun viewState(): LiveData<RecommendationsViewState> = viewState
    fun getNavigationEffect(): LiveData<Event<RecommendationsNavigationEffect>> = navigationEffect
    fun getViewEffect(): LiveData<Event<RecommendationsViewEffect>> = viewEffect
}