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

    var listener: StockOpnameListener? = null

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
    ) {
        dataStokOpnameRepository.createStokOpname(
            compositeDisposable,
            apiKey,
            token,
            itemNum,
            notes,
            stock,
            kondisi,
            status = object : CreateStockListener {
                override fun onSuccess() {
                    listener?.onSuccess()
                }

                override fun onFailure(message: String) {
                    listener?.onFailure(message)
                }

            }
        )
    }

    val networkState = dataStokOpnameRepository.networkState

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}