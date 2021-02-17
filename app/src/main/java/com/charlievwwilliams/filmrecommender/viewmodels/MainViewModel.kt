package com.charlievwwilliams.filmrecommender.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.charlievwwilliams.filmrecommender.api.RetrofitInstance
import com.charlievwwilliams.filmrecommender.extensions.Event
import com.charlievwwilliams.filmrecommender.model.search.Search
import com.charlievwwilliams.filmrecommender.utils.Constants.Companion.API_KEY
import com.charlievwwilliams.filmrecommender.viewstates.MainNavigationEffect
import com.charlievwwilliams.filmrecommender.viewstates.MainNavigationEffect.NavigateToCalculatorEffect
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEffect
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEffect.FilmSearchedEffect
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEvent
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEvent.ScreenLoadEvent
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEvent.SplashButtonPressedEvent
import com.charlievwwilliams.filmrecommender.viewstates.MainViewState
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
            is SplashButtonPressedEvent -> navigationEffect.value =
                Event(NavigateToCalculatorEffect)
        }
    }

    private fun onScreenLoad() {
        onScreenLoadSuccess()
    }

    fun tempFunction(input: String) {
        // IO (Network request), MainThread (UI), Default (Heavy Computation)
        CoroutineScope(IO).launch {
            val result = getResultFromAPI(input)
            // Go to main for UI Operations
            withContext(Main) {
                tempFunctionTwo(result)
            }
        }
    }

    private fun tempFunctionTwo(result: Search) {
        viewEffect.value = Event(FilmSearchedEffect(result))
    }

    private fun onScreenLoadSuccess() {
        viewState.value = MainViewState(
            isLoading = false
        )
    }

    private suspend fun getResultFromAPI(input: String): Search {
        delay(1000)
        /*return RetrofitInstance.Api.getMovieDetails(API_KEY)*/
        return RetrofitInstance.Api.searchMovies(API_KEY, "en-US", input, "1", "true")
    }

    fun viewState(): LiveData<MainViewState> = viewState
    fun getNavigationEffect(): LiveData<Event<MainNavigationEffect>> = navigationEffect
    fun getViewEffect(): LiveData<Event<MainViewEffect>> = viewEffect
}