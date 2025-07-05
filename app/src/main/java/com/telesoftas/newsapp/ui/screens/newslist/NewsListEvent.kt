package com.telesoftas.newsapp.ui.screens.newslist

sealed class NewsListEvent {
    data class ShowError(val message: String) : NewsListEvent()
}