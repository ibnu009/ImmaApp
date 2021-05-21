package com.pjb.immaapp.ui.usulanpermintaanbarang.material.approval

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pjb.immaapp.data.entity.notification.NotificationMessage
import com.pjb.immaapp.data.entity.notification.NotificationModel
import com.pjb.immaapp.main.MainRepository
import io.reactivex.disposables.CompositeDisposable

class ApprovalRabViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val mainRepository: MainRepository
) : ViewModel() {

     fun sendMessage(recipientToken: String, body: String, message: String, title: String) {
        val disposable = mainRepository.sendNotification(recipientToken, body, message, title)
            .subscribe()
        compositeDisposable.add(disposable)
    }

    fun getTokenData(apiKey: String, token: String): LiveData<List<NotificationModel>> {
        return mainRepository.getTokenData(compositeDisposable, apiKey, token)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}