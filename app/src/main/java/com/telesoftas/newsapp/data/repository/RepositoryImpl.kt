package com.telesoftas.newsapp.data.repository

import com.telesoftas.newsapp.data.networking.api.Api
import com.telesoftas.newsapp.data.networking.request.TopHeadlinesParams
import com.telesoftas.newsapp.data.networking.response.TopHeadlinesResponse
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: Api
) : Repository {
    override suspend fun getTopHeadlines(
        params: TopHeadlinesParams
    ): Response<TopHeadlinesResponse> {
        return api.getTopHeadlines(params.toQueryMap())
    }
}