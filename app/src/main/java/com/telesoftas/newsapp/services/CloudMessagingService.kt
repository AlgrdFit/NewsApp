package com.telesoftas.newsapp.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CloudMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("CloudMessagingService", "firebase token registered: $token")
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("CloudMessagingService", "Message received: $remoteMessage.")
    }
}