package com.telesoftas.newsapp.data.networking.api

import com.telesoftas.newsapp.data.networking.response.TopHeadlinesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface Api {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @QueryMap params: Map<String, String>
    ): Response<TopHeadlinesResponse>
}