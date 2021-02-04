package com.pjb.immaapp.data.source.po

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.pjb.immaapp.data.entity.po.ItemPurchaseOrder
import com.pjb.immaapp.webservice.po.PurchaseOrderService
import io.reactivex.disposables.CompositeDisposable

class PoItemDataSourceFactory(
    private val apiService: PurchaseOrderService,
    private val compositeDisposable: CompositeDisposable,
    private val token: String,
    private val ponum: String
) : DataSource.Factory<Int, ItemPurchaseOrder>() {

    val poLiveDataSource = MutableLiveData<PoItemDataSource>()

    override fun create(): DataSource<Int, ItemPurchaseOrder> {
        val poItemDataSource = PoItemDataSource(apiService, compositeDisposable, token, ponum)
        poLiveDataSource.postValue(poItemDataSource)

        return poItemDataSource
    }
}