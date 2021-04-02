package com.pjb.immaapp.data.source.usulanpermintaan

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.pjb.immaapp.data.entity.upb.Supplier
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.webservice.RetrofitApp.Companion.API_KEY
import com.pjb.immaapp.webservice.RetrofitApp.Companion.FIRST_PAGE
import com.pjb.immaapp.webservice.RetrofitApp.Companion.ITEM_PER_PAGE
import com.pjb.immaapp.webservice.usulan.UsulanPermintaanBarangService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*

class SupplierDataSource(
    private val apiService: UsulanPermintaanBarangService,
    private val compositeDisposable: CompositeDisposable,
    private val token: String
): PageKeyedDataSource<Int, Supplier>() {

    private val page = FIRST_PAGE
    private val apiKey = API_KEY.toString()

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Supplier>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.requestDataSupplier(apiKey, token).subscribeOn(Schedulers.io()).subscribe(
                {
                    if (it.data.size < ITEM_PER_PAGE) {
                        callback.onResult(it.data, null, null)
                        Timber.d("Check data : $it")
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        callback.onResult(it.data, null, FIRST_PAGE + 1)
                        networkState.postValue(NetworkState.LOADED)
                    }
                } , {
                    Timber.e("Error $it")
                    networkState.postValue(NetworkState.ERROR)
                }
            )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Supplier>) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Supplier>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.requestDataSupplier(apiKey, token).subscribeOn(Schedulers.io()).subscribe(
                {
                    if (it.data.size < ITEM_PER_PAGE) {
                        callback.onResult(it.data, null)
                        Timber.d("Check data $it")
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        val emptyList = Collections.emptyList<Supplier>()
                        callback.onResult(emptyList, params.key + 1)
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