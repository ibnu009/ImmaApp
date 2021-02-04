package com.pjb.immaapp.ui.purchaseorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.pjb.immaapp.data.entity.po.HeaderPurchaseOrder
import com.pjb.immaapp.data.entity.po.ItemPurchaseOrder
import com.pjb.immaapp.data.entity.po.PurchaseOrder
import com.pjb.immaapp.data.repository.DataPoRepository
import com.pjb.immaapp.utils.NetworkState
import io.reactivex.disposables.CompositeDisposable

class PurchaseOrderViewModel(
    private val dataPoRepository: DataPoRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    fun getListDataPo(
        token: String,
        keywords: String?,
    ): LiveData<PagedList<PurchaseOrder>> {
        return dataPoRepository.requestDataListPo(compositeDisposable, token, keywords)
    }

    fun getDetailDataPo(
        apiKey: String,
        token: String,
        ponum: String
    ): LiveData<HeaderPurchaseOrder> {
        return dataPoRepository.requestDetailDataPo(compositeDisposable, apiKey, token, ponum)
    }

    fun getListItemPo(
        token: String,
        ponum: String
    ): LiveData<PagedList<ItemPurchaseOrder>> {
        return dataPoRepository.requestItemInDetailDataPo(compositeDisposable, token, ponum)
    }

    fun listIsEmpty(
        token: String,
        keywords: String?
    ): Boolean {
        return getListDataPo(token, keywords).value?.isEmpty() ?: true
    }

    fun listItemIsEmty(token: String, ponum: String): Boolean {
        return getListItemPo(token, ponum).value?.isEmpty() ?: true
    }

    val networkState: LiveData<NetworkState> by lazy {
        dataPoRepository.getNetWorkState()
    }

    val networkStateDetail: LiveData<NetworkState> by lazy {
        dataPoRepository.networkState
    }

    val netWorkItemPo: LiveData<NetworkState> by lazy {
        dataPoRepository.getPoItemNetworkSate()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}