package com.pjb.immaapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.pjb.immaapp.data.entity.notification.NotificationMessage
import com.pjb.immaapp.data.entity.notification.NotificationModel
import com.pjb.immaapp.data.remote.response.ResponseRegistrationToken
import com.pjb.immaapp.service.webservice.RetrofitApp
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

class MainRepository {
    private val apiService = RetrofitApp.getNotificationService()
    private val apiServiceFirebase = RetrofitApp.getFirebaseNotificationService()

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository()
            }
    }

    fun uploadTokenToServer(
        apiKey: String,
        token: String,
        name: String,
        idUser: String,
        fcmToken: String,
    ): Single<ResponseRegistrationToken> {
        val mApiKey = apiKey.toRequestBody(MultipartBody.FORM)
        val mToken = token.toRequestBody(MultipartBody.FORM)
        val mName = name.toRequestBody(MultipartBody.FORM)
        val mIdUser = idUser.toRequestBody(MultipartBody.FORM)
        val mFcmToken = fcmToken.toRequestBody(MultipartBody.FORM)

        return apiService.saveRegistrationToken(
            apiKey = mApiKey,
            token = mToken,
            name = mName,
            idUser = mIdUser,
            tokenFcm = mFcmToken
        )
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn {
                ResponseRegistrationToken(message = it.message!!, status = 0)
            }
            .subscribeOn(Schedulers.io())
    }

    fun getTokenData(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String
    ): LiveData<List<NotificationModel>> {
        val notificationModels = MutableLiveData<List<NotificationModel>>()
        compositeDisposable.add(apiService.getAuthoritiesToken(apiKey, token)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                notificationModels.postValue(it.data)
            }, {})
        )
        return notificationModels
    }

    fun sendNotification(recipientToken: String, body: String, message: String, title: String): Single<String> {
        val jsonObject = buildJsonObject(
            to = recipientToken,
            body = body,
            message = message,
            title = title
        )
       return apiServiceFirebase.sendMessage(jsonObject)
           .observeOn(AndroidSchedulers.mainThread())
           .subscribeOn(Schedulers.io())
           .onErrorReturn {
               it.message
           }
    }

    private fun buildJsonObject(title: String, body: String, message: String, to: String): JsonObject {
        val payload = JsonObject()
        payload.addProperty("to", to)

        val data = JsonObject()
        data.addProperty("title", title)
        data.addProperty("body", body)
        data.addProperty("message", message)

        payload.add("data", data)

        return payload
    }
}