package com.telesoftas.newsapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.telesoftas.newsapp.data.networking.response.Article
import com.telesoftas.newsapp.ui.screens.newsdetails.NewsDetailsScreen
import com.telesoftas.newsapp.ui.screens.newslist.NewsListScreen
import com.telesoftas.newsapp.ui.screens.webview.WebViewScreen
import ektif.detectionreaction.utils.GenericNavType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
sealed class Screen {
    @Serializable
    data object NewsList : Screen()

    @Serializable
    data class NewsDetails(val article: Article) : Screen()

    @Serializable
    data class WebView(val url: String) : Screen()
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onSnackbar: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NewsList,
        modifier = modifier
    ) {
        composable<Screen.NewsList> {
            NewsListScreen(
                modifier = modifier,
                navController = navController,
                onSnackbar = onSnackbar,
            )
        }
        composable<Screen.NewsDetails>(
            typeMap = mapOf(
                typeOf<Article>() to GenericNavType(Article.serializer())
            )
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.NewsDetails>()
            NewsDetailsScreen(
                article = args.article,
                navController = navController,
            )
        }
        composable<Screen.WebView> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.WebView>()
            WebViewScreen(
                url = args.url,
            )
        }
    }
}