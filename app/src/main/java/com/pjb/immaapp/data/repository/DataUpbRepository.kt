package com.pjb.immaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.pjb.immaapp.data.entity.upb.PermintaanBarang
import com.pjb.immaapp.data.source.usulanpermintaan.UpbDataSource
import com.pjb.immaapp.data.source.usulanpermintaan.UpbDataSourceFactory
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.webservice.RetrofitApp
import com.pjb.immaapp.webservice.RetrofitApp.Companion.ITEM_PER_PAGE
import io.reactivex.disposables.CompositeDisposable

class DataUpbRepository {
    private val apiService = RetrofitApp.getUpbService()
    private lateinit var upbDataSourceFactory : UpbDataSourceFactory
    companion object {
        @Volatile
        private var instance : DataUpbRepository? = null
        fun getInstance() : DataUpbRepository =
            instance ?: synchronized(this) {
                instance ?: DataUpbRepository()
            }
    }

    fun requestDataListUpb(
        compositeDisposable: CompositeDisposable,
        token : String,
        keywords : String?
    ): LiveData<PagedList<PermintaanBarang>>{
        lateinit var resultDataUpb : LiveData<PagedList<PermintaanBarang>>

        upbDataSourceFactory = UpbDataSourceFactory(apiService, compositeDisposable, token, keywords)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ITEM_PER_PAGE)
            .build()

        resultDataUpb = LivePagedListBuilder(upbDataSourceFactory, config).build()

        return resultDataUpb
    }

    fun getNetworkState() : LiveData<NetworkState>{
        return Transformations.switchMap(
            upbDataSourceFactory.usulanPermintaanLiveDataSource,
            UpbDataSource::networkState
        )
    }
}