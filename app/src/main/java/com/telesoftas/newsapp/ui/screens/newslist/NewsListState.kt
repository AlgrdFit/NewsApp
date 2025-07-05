package com.telesoftas.newsapp.ui.screens.newslist

import com.telesoftas.newsapp.data.networking.response.Article

data class NewsListState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val articles: List<Article> = emptyList(),
    val isEndReached: Boolean = false,
)