package com.pjb.immaapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
}