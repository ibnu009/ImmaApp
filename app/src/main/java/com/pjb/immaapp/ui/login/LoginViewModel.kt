package com.pjb.immaapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pjb.immaapp.data.entity.User
import com.pjb.immaapp.data.entity.request.Credential
import com.pjb.immaapp.data.repository.LoginRepository
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    fun getLoginRequest(credential: Credential): LiveData<User> {
        return loginRepository.requestLogin(credential, compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}