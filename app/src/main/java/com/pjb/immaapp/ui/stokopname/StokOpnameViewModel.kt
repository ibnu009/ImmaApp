package com.pjb.immaapp.ui.stokopname

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pjb.immaapp.data.entity.stockopname.StockOpname
import com.pjb.immaapp.data.remote.response.ResponseCreateStokOpname
import com.pjb.immaapp.data.repository.DataStokOpnameRepository
import io.reactivex.disposables.CompositeDisposable

class StokOpnameViewModel(
    private val dataStokOpnameRepository: DataStokOpnameRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    fun getDataStokOpname(
        apiKey: String,
        token: String,
        itemNum: Int
    ): LiveData<StockOpname> {
        return dataStokOpnameRepository.requestGetDataStockOpname(
            compositeDisposable,
            apiKey,
            token,
            itemNum
        )
    }

    fun addDataStokOpname(
        apiKey: String,
        token: String,
        itemNum: Int,
        notes: String,
        stock: Int,
        kondisi: String
    ): LiveData<ResponseCreateStokOpname> {
        return dataStokOpnameRepository.createStokOpname(
            compositeDisposable,
            apiKey,
            token,
            itemNum,
            notes,
            stock,
            kondisi
        )
    }

    val networkState by lazy {
        dataStokOpnameRepository.networkState
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}