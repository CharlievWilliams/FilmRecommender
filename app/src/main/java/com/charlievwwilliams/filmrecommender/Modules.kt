package com.charlievwwilliams.filmrecommender

import com.charlievwwilliams.filmrecommender.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

}

val viewModelModule = module {
    viewModel { MainViewModel() }
}