package com.pjb.immaapp.data.source.usulanpermintaan

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.pjb.immaapp.data.entity.upb.ItemPermintaanBarang
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.webservice.RetrofitApp
import com.pjb.immaapp.webservice.RetrofitApp.Companion.ITEM_PER_PAGE
import com.pjb.immaapp.webservice.usulan.UsulanPermintaanBarangService
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*

class UpbItemDataSource(
    private val apiService: UsulanPermintaanBarangService,
    private val compositeDisposable: CompositeDisposable,
    private val token: String,
    private val idPermintaan: Int
) : PageKeyedDataSource<Int, ItemPermintaanBarang>() {

    private val apiKey = RetrofitApp.API_KEY.toString()

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ItemPermintaanBarang>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.requestDetailUpb(
                apiKey = apiKey,
                token = token,
                idPermintaan = idPermintaan
            ).subscribeOn(Schedulers.io()).subscribe(
                {
                    if (it.data.size > ITEM_PER_PAGE) {
                        Timber.d("DataSize : ${it.data.size}")
                        callback.onResult(it.data, null, null)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        Timber.d("DataSize : ${it.data.size}")
                        callback.onResult(it.data, null, null)
                        networkState.postValue(NetworkState.LOADED)
                    }
                }, {
                    Timber.e("Error $it")
                    networkState.postValue(NetworkState.ERROR)
                }
            )
        )
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ItemPermintaanBarang>
    ) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ItemPermintaanBarang>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.requestDetailUpb(
                apiKey = apiKey,
                token = token,
                idPermintaan = idPermintaan
            ).subscribeOn(Schedulers.io()).subscribe(
                {
                    if (it.data.size > ITEM_PER_PAGE) {
                        Timber.d("DataSize : ${it.data.size}")
                        callback.onResult(it.data, null)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        Timber.d("DataSize : ${it.data.size}")
                        val emptyList = Collections.emptyList<ItemPermintaanBarang>()
                        callback.onResult(emptyList, null)
                        networkState.postValue(NetworkState.LOADED)
                    }
                } , {
                    Timber.e("Error : $it")
                    networkState.postValue(NetworkState.ERROR)
                }
            )
        )
    }

}