package com.charlievwwilliams.filmrecommender.api

import com.charlievwwilliams.filmrecommender.model.django.Django
import com.charlievwwilliams.filmrecommender.model.movies.details.Details
import com.charlievwwilliams.filmrecommender.model.search.Search
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("3/search/movie")
    suspend fun searchMovies(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("query") query: String,
        @Query("page") page: String,
        @Query("include_adult") includeAdult: String
    ): Search

    @GET("3/movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: String,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Details

    @GET("movies/")
    suspend fun getRecommendations(
        @Query("id") id: String,
        @Query("title") title: Boolean,
        @Query("genres") genres: Boolean,
        @Query("production_companies") productionCompanies: Boolean,
        @Query("spoken_languages") spokenLanguages: Boolean,
        @Query("keywords") keywords: Boolean,
        @Query("credits") credits: Boolean
    ): Django
}