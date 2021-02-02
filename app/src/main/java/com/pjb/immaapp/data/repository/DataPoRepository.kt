package com.pjb.immaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.pjb.immaapp.data.entity.PurchaseOrder
import com.pjb.immaapp.data.source.po.PoDataSource
import com.pjb.immaapp.data.source.po.PoDataSourceFactory
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.webservice.RetrofitApp
import com.pjb.immaapp.webservice.RetrofitApp.Companion.ITEM_PER_PAGE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class DataPoRepository {
    private val apiService = RetrofitApp.getPurchaseOrderService()
    private lateinit var poDataSourceFactory: PoDataSourceFactory
    companion object {
        @Volatile
        private var instance: DataPoRepository? = null
        fun getInstance(): DataPoRepository =
            instance ?: synchronized(this) {
                instance ?: DataPoRepository()
            }
    }


    fun requestDataListPo(
        compositeDisposable: CompositeDisposable,
        token: String,
        keywords: String?
    ): LiveData<PagedList<PurchaseOrder>> {
        lateinit var resultDataPo : LiveData<PagedList<PurchaseOrder>>
        poDataSourceFactory = PoDataSourceFactory(apiService, compositeDisposable, token, keywords)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ITEM_PER_PAGE)
            .build()

        resultDataPo = LivePagedListBuilder(poDataSourceFactory, config).build()
        return resultDataPo
    }

    fun getNetWorkState(): LiveData<NetworkState> {
        return Transformations.switchMap(
            poDataSourceFactory.poLiveDataSource,
            PoDataSource::networkState
        )
    }
}