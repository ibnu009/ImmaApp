package com.pjb.immaapp.ui.purchaseorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.pjb.immaapp.data.entity.PurchaseOrder
import com.pjb.immaapp.data.repository.DataPoRepository
import io.reactivex.disposables.CompositeDisposable

class PurchaseOrderViewModel(
    private val dataPoRepository: DataPoRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    fun getListDataPo(
        token: String,
        keywords: String?
    ): LiveData<PagedList<PurchaseOrder>> {
        return dataPoRepository.requestDataListPo(compositeDisposable, token, keywords)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}