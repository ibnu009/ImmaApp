package com.pjb.immaapp.ui.usulanpermintaanbarang.material.approval

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.Karyawan
import com.pjb.immaapp.data.entity.notification.NotificationMessage
import com.pjb.immaapp.data.entity.notification.NotificationModel
import com.pjb.immaapp.data.repository.DataUpbRepository
import com.pjb.immaapp.main.MainRepository
import com.pjb.immaapp.utils.ConverterHelper
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.utilsentity.GeneralErrorHandler
import io.reactivex.disposables.CompositeDisposable

class ApprovalRabViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val mainRepository: MainRepository,
    private val dataUpbRepository: DataUpbRepository
) : ViewModel() {

    var notes: String? = null
    var approvalRabListener: ApprovalRabListener? = null

    fun sendMessage(recipientToken: String, body: String?, message: String, title: String, context: Context) {
        val newBody = context.getString(R.string.note_empty_approval, title)
        val disposable = mainRepository.sendNotification(recipientToken, body ?: newBody, message, title)
            .subscribe()
        compositeDisposable.add(disposable)
    }

    fun getTokenData(apiKey: String, token: String): LiveData<List<NotificationModel>> {
        return mainRepository.getTokenData(compositeDisposable, apiKey, token)
    }

    fun saveRab(
        token: String,
        apiKey: String,
        idPermintaan: Int,
        idSdmApproval: Int,
    ) {
        approvalRabListener?.onInitiating()
        when {
            idPermintaan == 0 -> {
                approvalRabListener?.onFailure("Pastikan Permintaan yang dipilih Valid")
            }
            idSdmApproval == 0 -> {
                approvalRabListener?.onFailure("Pastikan Telah Memilih Approval List")
            }
            else -> {
                compositeDisposable.add(
                    dataUpbRepository.saveRab(
                        apiKey,
                        token,
                        idPermintaan,
                        idSdmApproval,
                        notes ?: "-"
                    ).subscribe(
                        {
                            if (it.status == 200) {
                                approvalRabListener?.onSuccess("RAB berhasil dibuat")
                            }
                        },
                        {
                            val message = ConverterHelper().convertExceptionToMessage(it)
                            approvalRabListener?.onFailure(message)
                        }
                    )
                )

            }
        }
    }

    fun getListKaryawan(apiKey: String, token: String): LiveData<List<Karyawan>> {
        val resultListKaryawan = MutableLiveData<List<Karyawan>>()
        compositeDisposable.add(
            dataUpbRepository.getListKaryawan(apiKey, token)
                .subscribe {
                    resultListKaryawan.postValue(it.data)
                }
        )
        return resultListKaryawan
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}