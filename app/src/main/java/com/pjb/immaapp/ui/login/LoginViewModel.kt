package com.pjb.immaapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pjb.immaapp.data.entity.User
import com.pjb.immaapp.data.entity.request.Credential
import com.pjb.immaapp.data.remote.response.ResponseUser
import com.pjb.immaapp.data.repository.LoginRepository
import com.pjb.immaapp.utils.ImmaEventHandler
import com.pjb.immaapp.utils.ImprovedNetworkState
import com.pjb.immaapp.utils.NetworkState
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    var username: String? = null
    var password: String? = null

    var authListener : AuthListener? = null

    private val _networkState = MutableLiveData<ImmaEventHandler<NetworkState>>()
    val getNetworkState: LiveData<ImmaEventHandler<NetworkState>>
        get() = _networkState

    private fun setNetworkState() {
        val event = ImmaEventHandler(loginRepository.networkStateTest)
        _networkState.postValue(event)
    }

    val networkState: LiveData<NetworkState> by lazy {
        loginRepository.networkState
    }

    fun logInAndStoreResult() {
        Timber.d("CHECKING LOGINANDSTORERESULT")
        authListener?.onAuthenticating()
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
        compositeDisposable.dispose()
    }
}


