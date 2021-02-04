package com.pjb.immaapp.data.source.po

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.pjb.immaapp.data.entity.po.ItemPurchaseOrder
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.webservice.RetrofitApp
import com.pjb.immaapp.webservice.RetrofitApp.Companion.ITEM_PER_PAGE
import com.pjb.immaapp.webservice.po.PurchaseOrderService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PoItemDataSource(
    private val apiService: PurchaseOrderService,
    private val compositeDisposable: CompositeDisposable,
    private val token: String,
    private val ponum: String
) : PageKeyedDataSource<Int, ItemPurchaseOrder>() {

    private val page = RetrofitApp.FIRST_PAGE
    private val apiKey = RetrofitApp.API_KEY.toString()

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Int>,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, ItemPurchaseOrder>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.requestDetailPurchaseOrder(
                apiKey = apiKey,
                token = token,
                ponum = ponum
            ).subscribeOn(Schedulers.io()).subscribe(
                {
                    if (it.data.size < ITEM_PER_PAGE) {
                        callback.onResult(it.data, null, null)
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

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, ItemPurchaseOrder>
    ) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, ItemPurchaseOrder>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.requestDetailPurchaseOrder(
                apiKey = apiKey,
                token = token,
                ponum = ponum)
                .subscribeOn(Schedulers.io())
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