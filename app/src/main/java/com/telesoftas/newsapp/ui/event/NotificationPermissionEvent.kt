package com.telesoftas.newsapp.ui.event

sealed class NotificationPermissionEvent {
    object RequestPermission : NotificationPermissionEvent()
    object PermissionGranted : NotificationPermissionEvent()
    object PermissionDenied : NotificationPermissionEvent()
}