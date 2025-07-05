package com.telesoftas.newsapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.telesoftas.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.launch

@Composable
fun NewsApp() {
    NewsAppTheme {
        val navController = rememberNavController()

        val snackbarHostState = remember { SnackbarHostState() }
        val snackbarCoroutineScope = rememberCoroutineScope()
        fun showSnackbar(message: String) {
            snackbarCoroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            AppNavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                onSnackbar = ::showSnackbar
            )
        }
    }
}