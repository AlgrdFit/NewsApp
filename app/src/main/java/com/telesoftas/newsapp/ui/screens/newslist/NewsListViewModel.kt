package com.telesoftas.newsapp.ui.screens.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telesoftas.newsapp.data.networking.request.TopHeadlinesParams
import com.telesoftas.newsapp.data.networking.response.Article
import com.telesoftas.newsapp.data.repository.Repository
import com.telesoftas.newsapp.utlis.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val repository: Repository,
    private val dateFormatter: DateFormatter,
) : ViewModel() {
    private val _event = MutableSharedFlow<NewsListEvent>()
    val event = _event.asSharedFlow()

    private val _state = MutableStateFlow(NewsListState())
    val state = _state.asStateFlow()

    private var currentPage = 1
    private var isLoadingMore = false

    init {
        onEndOfList()
    }

    fun onEndOfList() {
        if (isLoadingMore || state.value.isEndReached) return

        isLoadingMore = true
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = repository.getTopHeadlines(TopHeadlinesParams(page = currentPage))
                if (response.isSuccessful) {
                    val articlesResponse = response.body()?.articles.orEmpty()
                    val articles = formatArticles(articlesResponse)
                    _state.update {
                        it.copy(
                            articles = it.articles + articles,
                            isLoading = false,
                            isRefreshing = false,
                            isEndReached = articles.isEmpty()
                        )
                    }
                    currentPage++
                } else {
                    val error = response.errorBody()?.string()
                    _state.update { it.copy(isLoading = false, isRefreshing = false) }
                    _event.emit(NewsListEvent.ShowError(error ?: "Unknown error"))
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, isRefreshing = false) }
                _event.emit(NewsListEvent.ShowError(e.message ?: "Unknown error"))
            } finally {
                isLoadingMore = false
            }
        }
    }

    private fun formatArticles(articles: List<Article>): List<Article> {
        return articles.map { article ->
            article.copy(
                publishedAt = dateFormatter.formatDate(article.publishedAt.orEmpty())
            )
        }
    }

    fun onRefresh() {
        currentPage = 1
        _state.update { it.copy(articles = emptyList(), isEndReached = false, isRefreshing = true) }
        onEndOfList()
    }
}