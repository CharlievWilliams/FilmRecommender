package com.charlievwwilliams.filmrecommender

import com.charlievwwilliams.filmrecommender.viewmodels.MainViewModel
import com.charlievwwilliams.filmrecommender.viewmodels.RecommendationsViewModel
import com.charlievwwilliams.filmrecommender.viewmodels.SearchResultViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

}

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { SearchResultViewModel() }
    viewModel { RecommendationsViewModel() }
}