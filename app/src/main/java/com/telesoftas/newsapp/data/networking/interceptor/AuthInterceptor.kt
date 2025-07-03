package com.telesoftas.newsapp.data.networking.interceptor

import com.telesoftas.newsapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", BuildConfig.NEWS_API_KEY)
            .build()

        return chain.proceed(newRequest)
    }
}