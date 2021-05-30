package com.pjb.immaapp.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.*
import com.pjb.immaapp.data.entity.local.notification.NotificationEntity
import com.pjb.immaapp.main.MainRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class NotificationViewModel(
    private val mainRepository: MainRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    var notificationListener: NotificationListener? = null

    fun getNotification(): LiveData<PagingData<NotificationEntity>> {
        return Pager(
            PagingConfig(20),
            1,
            mainRepository.getNotificationList().asPagingSourceFactory(Dispatchers.IO)
        ).liveData
    }

    fun deleteNotification(id: Int) {
        mainRepository.deleteNotification(id)
    }
}