package com.pjb.immaapp.data.source.po

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.pjb.immaapp.data.entity.po.PurchaseOrder
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.webservice.RetrofitApp
import com.pjb.immaapp.webservice.RetrofitApp.Companion.API_KEY
import com.pjb.immaapp.webservice.RetrofitApp.Companion.FIRST_PAGE
import com.pjb.immaapp.webservice.RetrofitApp.Companion.ITEM_PER_PAGE
import com.pjb.immaapp.webservice.po.PurchaseOrderService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*

class PoDataSource(
    private val apiService: PurchaseOrderService,
    private val compositeDisposable: CompositeDisposable,
    private val token: String,
    private val keyword: String?
): PageKeyedDataSource<Int, PurchaseOrder>() {

    private val page = FIRST_PAGE
    private val apiKey = API_KEY.toString()

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PurchaseOrder>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.requestListPurchaseOrder(
                apiKey = apiKey,
                token = token,
                keywords = keyword
            ).subscribeOn(Schedulers.io()).subscribe(
                {
                    if (it.data.size < RetrofitApp.ITEM_PER_PAGE) {
                        callback.onResult(it.data, null, null)
                        networkState.postValue(NetworkState.LOADED)
                    }else{
                        callback.onResult(it.data, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)
                        Timber.d("Data Size adalah : ${it.data.size}")

                    }
                }, {
                    Timber.e("Error $it")
                    networkState.postValue(NetworkState.ERROR)
                }
            )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PurchaseOrder>) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PurchaseOrder>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.requestListPurchaseOrder(
                apiKey = apiKey,
                token = token,
                keywords = keyword)
                .subscribeOn(
                Schedulers.io())
                .subscribe(
                {
                    if (it.data.size < ITEM_PER_PAGE) {
                        Timber.d("Data Size adalah : ${it.data.size}")
                        callback.onResult(it.data, null)
                        networkState.postValue(NetworkState.LOADED)
                    } else{
                        Timber.d("Data Size adalah : ${it.data.size}")
                        val emptyList = Collections.emptyList<PurchaseOrder>()
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