package com.pjb.immaapp.data.source.po

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.pjb.immaapp.data.entity.local.po.PurchaseOrders
import com.pjb.immaapp.utils.PoMapper
import com.pjb.immaapp.webservice.po.PurchaseOrderService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PoRxDataSource(
    private val apiService: PurchaseOrderService,
    private val mapper: PoMapper,
    private val apiKey: String,
    private val token: String,
    private val keywords: String?
) : RxPagingSource<Int, PurchaseOrders.PurchaseOrderEntity>() {

    override fun getRefreshKey(state: PagingState<Int, PurchaseOrders.PurchaseOrderEntity>): Int? {
        TODO("Not yet implemented")
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, PurchaseOrders.PurchaseOrderEntity>> {
        return apiService.requestListPurchaseOrderSingle(apiKey, token, keywords)
            .subscribeOn(Schedulers.io())
            .map { mapper.transform(it) }
            .map { toLoadResult(it) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        data: PurchaseOrders
    ): LoadResult<Int, PurchaseOrders.PurchaseOrderEntity> {
        return LoadResult.Page(
            data = data.data,
            prevKey = null,
            nextKey = null
        )
    }
}