package com.pjb.immaapp.data.source.po

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.pjb.immaapp.data.entity.po.PurchaseOrder
import com.pjb.immaapp.webservice.po.PurchaseOrderService
import io.reactivex.disposables.CompositeDisposable

class PoDataSourceFactory(
    private val apiService: PurchaseOrderService,
    private val compositeDisposable: CompositeDisposable,
    private val token: String,
    private val keyword: String?
): DataSource.Factory<Int, PurchaseOrder>() {
    val poLiveDataSource = MutableLiveData<PoDataSource>()

    override fun create(): DataSource<Int, PurchaseOrder> {
        val poDataSource = PoDataSource(apiService, compositeDisposable, token, keyword)

        poLiveDataSource.postValue(poDataSource)
        return poDataSource
    }
}