package com.pjb.immaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pjb.immaapp.data.entity.stockopname.StockOpname
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.webservice.RetrofitApp
import com.pjb.immaapp.webservice.stockopname.StockOpnameService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class DataStokOpnameRepository {
    private val apiService = RetrofitApp.getStockOpnameService()
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    companion object {
        @Volatile
        private var instance: DataStokOpnameRepository? = null
        fun getInstance(): DataStokOpnameRepository =
            instance ?: synchronized(this) {
                instance ?: DataStokOpnameRepository()
            }
    }

    fun requestGetDataStockOpname(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String,
        itemNum: Int
    ): LiveData<StockOpname> {
        networkState.postValue(NetworkState.LOADING)
        val resultData = MutableLiveData<StockOpname>()
        compositeDisposable.add(
            apiService.getStockOpnameResponse(apiKey, token, itemNum)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map {
                    it.data
                }
                .subscribe(
                    {
                        resultData.postValue(it[0])
                        networkState.postValue(NetworkState.LOADED)
                    }, {
                        Timber.e(it)
                        networkState.postValue(NetworkState.ERROR)
                    }
                ))
        return resultData
    }

}