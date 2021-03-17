package com.charlievwwilliams.filmrecommender.api

import com.charlievwwilliams.filmrecommender.utils.Constants.Companion.BASE_URL
import com.charlievwwilliams.filmrecommender.utils.Constants.Companion.DJANGO_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val filmRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val filmApi: ApiService by lazy {
        filmRetrofit.create(ApiService::class.java)
    }

    private val djangoRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(DJANGO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val djangoApi: ApiService by lazy {
        djangoRetrofit.create(ApiService::class.java)
    }
}