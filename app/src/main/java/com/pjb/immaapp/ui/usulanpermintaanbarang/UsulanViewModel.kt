package com.pjb.immaapp.ui.usulanpermintaanbarang

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.pjb.immaapp.data.entity.upb.PermintaanBarang
import com.pjb.immaapp.data.repository.DataUpbRepository
import com.pjb.immaapp.utils.NetworkState
import io.reactivex.disposables.CompositeDisposable

class UsulanViewModel(
    private val dataUpbRepository : DataUpbRepository,
    private val compositeDisposable: CompositeDisposable
): ViewModel(){

    fun getListDataUpb(
        token : String,
        keyword : String?
    ) : LiveData<PagedList<PermintaanBarang>>{
        return dataUpbRepository.requestDataListUpb(compositeDisposable, token, keyword)
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
        super.onCleared()
    }
}