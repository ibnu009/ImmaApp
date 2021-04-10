package com.pjb.immaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pjb.immaapp.data.entity.User
import com.pjb.immaapp.data.entity.request.Credential
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.global.ImmaEventHandler
import com.pjb.immaapp.utils.utilsentity.GeneralErrorHandler
import com.pjb.immaapp.service.webservice.RetrofitApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LoginRepository {
    private val apiService = RetrofitApp.getLoginService()
    var networkStateRepo: ImmaEventHandler<NetworkState> = ImmaEventHandler()
    private val resultUser = MutableLiveData<User>()


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
        Timber.d("Loading")
        networkStateRepo.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.loginRequest(credential)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Timber.d("Get User")
                    resultUser.postValue(it.data)
                    networkStateRepo.postValue(NetworkState.LOADED)
                }, {
                    Timber.e("ErrorLogin is $it")
                    val error = GeneralErrorHandler().getError(it)
                    networkStateRepo.postValue(error)
                })
        )
        return resultUser
    }
}