package com.telesoftas.newsapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telesoftas.newsapp.ui.event.NotificationPermissionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<NotificationPermissionEvent>()
    val event = _event.asSharedFlow()

    fun checkNotificationPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                viewModelScope.launch {
                    _event.emit(NotificationPermissionEvent.RequestPermission)
                }
            }
        }
    }

    fun onPermissionResult(granted: Boolean) {
        viewModelScope.launch {
            _event.emit(
                if (granted) {
                    NotificationPermissionEvent.PermissionGranted
                } else {
                    NotificationPermissionEvent.PermissionDenied
                }
            )
        }
    }
}