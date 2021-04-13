package com.pjb.immaapp.ui.login

import androidx.lifecycle.*
import com.pjb.immaapp.data.entity.request.Credential
import com.pjb.immaapp.data.repository.LoginRepository
import com.pjb.immaapp.utils.network.NetworkEvent
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.global.ImmaEventHandler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber


class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    var username: String? = null
    var password: String? = null

    private val singleEvent = loginRepository.networkStateRepo
    fun checkState(): LiveData<NetworkState> {
        return singleEvent
    }

    var authListener: AuthListener? = null

    fun logInAndStoreResult() {
        Timber.d("CHECKING LOGINANDSTORERESULT")
        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Masih ada data yang kosong")
            return
        } else {
            val loginResult = loginRepository.requestLogin(getCredential(), compositeDisposable)
            authListener?.onSuccess(getCredential(), loginResult)
        }
    }

    private fun getCredential(): Credential {
        val apiKey = 12345
        return Credential(
            username = username!!,
            password = password!!,
            apiKey = apiKey.toString()
        )
    }

    override fun onCleared() {
        super.onCleared()
        NetworkEvent.unRegister(this)
        compositeDisposable.dispose()
    }
}


