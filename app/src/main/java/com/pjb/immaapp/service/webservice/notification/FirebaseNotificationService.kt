package com.pjb.immaapp.service.webservice.notification

import com.google.gson.JsonObject
import com.pjb.immaapp.BuildConfig
import com.pjb.immaapp.data.entity.notification.NotificationMessage
import com.pjb.immaapp.data.entity.notification.NotificationModel
import io.reactivex.Single
import retrofit2.http.*

interface FirebaseNotificationService {

    @Headers("Authorization:key=${BuildConfig.MESSAGING_API_KEY}", "Content-Type:application/json")
    @POST("fcm/send")
    fun sendMessage(
        @Body payload: JsonObject
    ): Single<String>
}