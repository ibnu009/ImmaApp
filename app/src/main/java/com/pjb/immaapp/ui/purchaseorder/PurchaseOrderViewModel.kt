package com.pjb.immaapp.ui.purchaseorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagedList
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.pjb.immaapp.data.entity.local.po.PurchaseOrders
import com.pjb.immaapp.data.entity.po.HeaderPurchaseOrder
import com.pjb.immaapp.data.entity.po.ItemPurchaseOrder
import com.pjb.immaapp.data.entity.po.PurchaseOrder
import com.pjb.immaapp.data.repository.DataPoRepository
import com.pjb.immaapp.utils.NetworkState
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

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

    @ExperimentalPagingApi
    fun getListDataPoPaging(): LiveData<PagingData<PurchaseOrders.PurchaseOrderEntity>> {
        val resultDataPo = MutableLiveData<PagingData<PurchaseOrders.PurchaseOrderEntity>>()
        compositeDisposable.add(dataPoRepository.requestDataPo().subscribe{
            resultDataPo.postValue(it)
            Timber.d("ReceivedVM $it")
        })
        return resultDataPo
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