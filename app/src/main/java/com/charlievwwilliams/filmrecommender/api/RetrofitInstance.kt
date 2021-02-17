package com.charlievwwilliams.filmrecommender.api

import com.charlievwwilliams.filmrecommender.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val Api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}