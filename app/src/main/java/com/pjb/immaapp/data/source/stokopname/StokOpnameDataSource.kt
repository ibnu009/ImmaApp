package com.pjb.immaapp.data.source.stokopname

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.pjb.immaapp.data.entity.stockopname.StockOpname
import com.pjb.immaapp.webservice.stockopname.StockOpnameService
import io.reactivex.disposables.CompositeDisposable

class StokOpnameDataSource(
    private val apiService: StockOpnameService,
    private val compositeDisposable: CompositeDisposable,
    private val token: String,
    private val itemNum: String
) : PageKeyedDataSource<Int, StockOpname>() {
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, StockOpname>) {
        TODO("Not yet implemented")
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, StockOpname>) {
        TODO("Not yet implemented")
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, StockOpname>
    ) {
        TODO("Not yet implemented")
    }
}