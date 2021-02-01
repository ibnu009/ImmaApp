package com.pjb.immaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pjb.immaapp.data.entity.User
import com.pjb.immaapp.data.entity.request.Credential
import com.pjb.immaapp.webservice.RetrofitApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LoginRepository {
    private val apiService = RetrofitApp.getLoginService()

    companion object {
        @Volatile
        private var instance: LoginRepository? = null
        fun getInstance(): LoginRepository =
            instance ?: synchronized(this) {
                instance ?: LoginRepository()
            }
    }

    fun requestLogin(
        credential: Credential,
        compositeDisposable: CompositeDisposable
    ): LiveData<User> {
        val resultUser = MutableLiveData<User>()
        compositeDisposable.add(apiService.loginRequest(credential)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { it.data }
            .subscribe({
                Timber.d("Get User")
                resultUser.postValue(it)
            }, {
                Timber.e("Error is $it")
            }
            ))
        return resultUser
    }
}