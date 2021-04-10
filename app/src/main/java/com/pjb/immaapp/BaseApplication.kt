package com.pjb.immaapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import net.gotev.uploadservice.UploadServiceConfig
import net.gotev.uploadservice.logger.UploadServiceLogger
import net.gotev.uploadservice.okhttp.OkHttpStack
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.concurrent.TimeUnit


class BaseApplication : Application() {

    companion object {
        const val notificationChannelID = "ImmaChannel"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                notificationChannelID,
                "Imma App Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        createNotificationChannel()

        UploadServiceConfig.initialize(
            context = this,
            defaultNotificationChannel = notificationChannelID,
            debug = BuildConfig.DEBUG

        )
        UploadServiceConfig.httpStack = getOkHttpClient()?.let { OkHttpStack(it) }!!
//        jika file yang upload terlalu besar maka besarkan menitnya
        UploadServiceConfig.idleTimeoutSeconds = 60 * 5
//        Buffer nya jangan terlalu besar ditakutkan terjadi leaking pada pipe
        UploadServiceConfig.bufferSizeBytes = 4096
        UploadServiceLogger.setLogLevel(UploadServiceLogger.LogLevel.Debug)

    }

    private fun getOkHttpClient(): OkHttpClient? {
        return OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(60 * 5, TimeUnit.SECONDS)
            .readTimeout(60 * 5, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor { message: String? ->
                if (message != null) {
                    Timber.tag("message").d(message)
                }
            }.setLevel(HttpLoggingInterceptor.Level.BODY))
            .cache(null)
            .build()
    }
}