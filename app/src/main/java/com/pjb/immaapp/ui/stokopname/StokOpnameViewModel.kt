package com.pjb.immaapp.ui.stokopname

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pjb.immaapp.data.entity.stockopname.StockOpname
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

    val networkState by lazy {
        dataStokOpnameRepository.networkState
    }

}