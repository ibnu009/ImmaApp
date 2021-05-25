package com.pjb.immaapp.service.firebaseservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.local.notification.NotificationEntity
import com.pjb.immaapp.data.local.db.ImmaDatabase
import com.pjb.immaapp.main.MainActivity
import timber.log.Timber

class FirebaseMessageService : FirebaseMessagingService() {

    private lateinit var immaDatabase: ImmaDatabase

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
            val type = p0.data["type"] ?: "unknown"
            val price = p0.data["price"] ?: "unknown"
            val companyName = p0.data["company_name"] ?: "unknown"
            val materialName = p0.data["material_name"] ?: "unknown"

            sendNotification(
                messageBody = message,
                messageTitle = senderName,
                type = type,
                price = price,
                companyName = companyName,
                materialName
            )
        }
    }

    private fun sendNotification(
        messageBody: String,
        messageTitle: String,
        type: String,
        price: String,
        companyName: String,
        materialName: String
    ) {
        var intent: Intent? = null
        if (type == "rab_request") {
            intent = Intent(this, MainActivity::class.java)
            immaDatabase =
                Room.databaseBuilder(applicationContext, ImmaDatabase::class.java, "db_imma")
                    .build()
            Thread {
                val notificationEntity = NotificationEntity(
                    sender = messageTitle,
                    message = messageBody,
                    price = price,
                    companyName = companyName,
                    materialName = materialName
                )
            }.start()
        } else {
            intent = Intent(this, MainActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo_pjb)
            .setContentTitle(getString(R.string.notif_message, messageTitle))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


}