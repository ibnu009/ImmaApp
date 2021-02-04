package com.pjb.immaapp.data.source.usulanpermintaan

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.pjb.immaapp.data.entity.upb.PermintaanBarang
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.webservice.RetrofitApp.Companion.API_KEY
import com.pjb.immaapp.webservice.RetrofitApp.Companion.FIRST_PAGE
import com.pjb.immaapp.webservice.RetrofitApp.Companion.ITEM_PER_PAGE
import com.pjb.immaapp.webservice.usulan.UsulanPermintaanBarangService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class UpbDataSource(
    private val apiService : UsulanPermintaanBarangService,
    private val compositeDisposable: CompositeDisposable,
    private val token: String,
    private val keyword : String?
    ): PageKeyedDataSource<Int, PermintaanBarang>() {
    private val page = FIRST_PAGE
    private val apiKey = API_KEY.toString()

    val networkState : MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PermintaanBarang>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.requestListUsulanPermintaanBarang(
                apiKey = apiKey,
                token = token,
                keywords = keyword
            ).subscribeOn(Schedulers.io()).subscribe(
                {
                    if (it.data.size < ITEM_PER_PAGE) {
                        callback.onResult(it.data, null, null)
                        Timber.d("Check data UPB : ${it.data}")
                        networkState.postValue(NetworkState.LOADED)
                    }else{
                        callback.onResult(it.data, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)
                    }
                }, {
                    Timber.e("Error $it")
                    networkState.postValue(NetworkState.ERROR)
                }
            )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PermintaanBarang>) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PermintaanBarang>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.requestListUsulanPermintaanBarang(
                apiKey = apiKey,
                token = token,
                keywords = keyword
            )
                .subscribeOn(
                    Schedulers.io()
                )
                .subscribe(
                    {
                        if (it.data.size < ITEM_PER_PAGE) {
                            callback.onResult(it.data, null)
                            networkState.postValue(NetworkState.LOADED)
                        } else{
                            callback.onResult(it.data, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        }
                    }, {
                        Timber.e("Error $it")
                        networkState.postValue(NetworkState.ERROR)
                    }
                )
        )
    }
}