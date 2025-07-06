package com.telesoftas.newsapp.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.telesoftas.newsapp.ui.event.NotificationPermissionEvent
import com.telesoftas.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NewsApp(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val event by mainViewModel.event.collectAsState(initial = null)
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarCoroutineScope = rememberCoroutineScope()
    fun showSnackbar(message: String) {
        snackbarCoroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        mainViewModel.onPermissionResult(isGranted)
    }

    LaunchedEffect(event) {
        when (event) {
            NotificationPermissionEvent.RequestPermission -> {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

            NotificationPermissionEvent.PermissionGranted -> {
                showSnackbar("Notifications enabled")
            }

            NotificationPermissionEvent.PermissionDenied -> {
                showSnackbar("Notifications are disabled. You can enable them in app settings.")
            }

            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        mainViewModel.checkNotificationPermission(context)
    }

    NewsAppTheme {
        val navController = rememberNavController()

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