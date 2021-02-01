package com.pjb.immaapp.data.source.po

import androidx.paging.PageKeyedDataSource
import com.pjb.immaapp.data.entity.PurchaseOrder
import com.pjb.immaapp.webservice.RetrofitApp.Companion.API_KEY
import com.pjb.immaapp.webservice.RetrofitApp.Companion.FIRST_PAGE
import com.pjb.immaapp.webservice.po.PurchaseOrderService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PoDataSource(
    private val apiService: PurchaseOrderService,
    private val compositeDisposable: CompositeDisposable,
    private val token: String,
    private val keyword: String?
): PageKeyedDataSource<Int, PurchaseOrder>() {
    private val page = FIRST_PAGE
    private val apiKey = API_KEY.toString()


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PurchaseOrder>
    ) {
        compositeDisposable.add(
            apiService.requestListPurchaseOrder(
                apiKey = apiKey,
                token = token,
                keywords = keyword
            ).subscribeOn(Schedulers.io()).subscribe(
                {
                    callback.onResult(it.data, null, page+1)
                },{
                    Timber.e("Error $it")
                }
            )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PurchaseOrder>) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PurchaseOrder>) {
        compositeDisposable.add(
            apiService.requestListPurchaseOrder(
                apiKey = apiKey,
                token = token,
                keywords = keyword
            ).subscribeOn(Schedulers.io()).subscribe(
                {
                    if (500 >= params.key) {
                        callback.onResult(it.data, params.key + 1)
                    } else{
                        Timber.d("End Of the list")
                    }
                }, {
                    Timber.e("Error $it")
                }
            )
        )
    }
}