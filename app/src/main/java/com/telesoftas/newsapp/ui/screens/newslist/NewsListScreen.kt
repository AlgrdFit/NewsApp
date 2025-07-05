package com.telesoftas.newsapp.ui.screens.newslist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.telesoftas.newsapp.ui.Screen
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: NewsListViewModel = hiltViewModel(),
    onSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val event by viewModel.event.collectAsState(initial = null)
    val listState = rememberLazyListState()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.onRefresh() },
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
            items(state.articles) { article ->
                NewsListItem(article = article, onClick = {
                    navController.navigate(Screen.NewsDetails(article))
                })
            }
            if (state.articles.isNotEmpty() && state.isLoading) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator(
                            modifier = modifier.size(64.dp)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { index ->
                if (index >= state.articles.size - 1 && !state.isLoading) {
                    viewModel.onEndOfList()
                }
            }
    }
    LaunchedEffect(event) {
        when (event) {
            is NewsListEvent.ShowError -> {
                onSnackbar((event as NewsListEvent.ShowError).message)
            }

            else -> {}
        }
    }
}