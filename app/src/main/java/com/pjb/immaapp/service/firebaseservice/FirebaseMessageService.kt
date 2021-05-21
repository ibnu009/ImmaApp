package com.pjb.immaapp.service.firebaseservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage
import com.pjb.immaapp.R
import com.pjb.immaapp.main.MainActivity
import timber.log.Timber

class FirebaseMessageService : FirebaseMessagingService() {
    
    override fun onNewToken(p0: String) {
        Timber.d("Token is is $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Timber.d("Message From : ${p0.from}")

        if (p0.data.isNotEmpty()) {
            Timber.d("Message data : ${p0.data}")
            val senderName = p0.data["title"] ?: "unknown"
            val message = p0.data["body"] ?: "ada permintaan barang dari $senderName"
            sendNotification(messageBody = message, messageTitle = senderName)
        }
    }

    private fun sendNotification(messageBody: String, messageTitle: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo_pjb)
            .setContentTitle(getString(R.string.notif_message, messageTitle))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }



}