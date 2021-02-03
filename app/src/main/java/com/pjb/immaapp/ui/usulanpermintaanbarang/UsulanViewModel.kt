package com.pjb.immaapp.ui.usulanpermintaanbarang

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.pjb.immaapp.data.entity.upb.HeaderUsulanPermintaanBarang
import com.pjb.immaapp.data.entity.upb.ItemPermintaanBarang
import com.pjb.immaapp.data.entity.upb.PermintaanBarang
import com.pjb.immaapp.data.repository.DataUpbRepository
import com.pjb.immaapp.utils.NetworkState
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class UsulanViewModel(
    private val dataUpbRepository : DataUpbRepository,
    private val compositeDisposable: CompositeDisposable
): ViewModel(){

    fun getListDataUpb(
        token : String,
        keyword : String?
    ): LiveData<PagedList<PermintaanBarang>> {
        return dataUpbRepository.requestDataListUpb(compositeDisposable, token, keyword)
    }

    fun getDetailDataUpb(
        apiKey: String,
        token: String,
        idPermintaan: Int
    ): LiveData<HeaderUsulanPermintaanBarang> {
        return dataUpbRepository.requestDataDetailUpb(compositeDisposable, apiKey, token, idPermintaan)
    }

    fun getListItemUpb(
        apiKey: String,
        token: String,
        idPermintaan: Int
    ): LiveData<List<ItemPermintaanBarang>> {
        return dataUpbRepository.requestItemInDetailUpb(compositeDisposable, apiKey, token, idPermintaan)
    }

    fun listIsEmpty(
        token : String,
        keyword: String?
    ) : Boolean {
        return getListDataUpb(token,keyword).value?.isEmpty() ?: true
    }

    val networkState : LiveData<NetworkState> by lazy {
        dataUpbRepository.getNetworkState()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        Timber.d("Data cleared")
        super.onCleared()
    }
}