package com.telesoftas.newsapp.data.repository

import com.telesoftas.newsapp.data.networking.request.TopHeadlinesParams
import com.telesoftas.newsapp.data.networking.response.TopHeadlinesResponse
import retrofit2.Response

interface Repository {
    suspend fun getTopHeadlines(params: TopHeadlinesParams): Response<TopHeadlinesResponse>
}