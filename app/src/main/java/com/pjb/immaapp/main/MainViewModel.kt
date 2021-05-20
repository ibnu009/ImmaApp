package com.pjb.immaapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pjb.immaapp.data.entity.notification.NotificationModel
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    fun updateActionBarTitle(title: String) = _title.postValue(title)

    fun storeFcmTokenToServer(
        apiKey: String,
        token: String,
        name: String,
        idUser: String,
        fcmToken: String,
    ){
        val disposable = mainRepository.uploadTokenToServer(apiKey, token, name, idUser, fcmToken)
            .subscribe()
        compositeDisposable.add(disposable)
    }

    fun getFcmTokenFromServer(
        apiKey: String,
        token: String
    ): LiveData<List<NotificationModel>> {
        return mainRepository.getTokenData(compositeDisposable, apiKey, token)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}