package com.pjb.immaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.pjb.immaapp.data.entity.upb.HeaderUsulanPermintaanBarang
import com.pjb.immaapp.data.entity.upb.ItemPermintaanBarang
import com.pjb.immaapp.data.entity.upb.PermintaanBarang
import com.pjb.immaapp.data.source.usulanpermintaan.UpbDataSource
import com.pjb.immaapp.data.source.usulanpermintaan.UpbDataSourceFactory
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.webservice.RetrofitApp
import com.pjb.immaapp.webservice.RetrofitApp.Companion.ITEM_PER_PAGE
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

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
        token: String,
        keywords: String?
    ): LiveData<PagedList<PermintaanBarang>> {

        lateinit var resultDataUpb : LiveData<PagedList<PermintaanBarang>>

        upbDataSourceFactory = UpbDataSourceFactory(apiService, compositeDisposable, token, keywords)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ITEM_PER_PAGE)
            .build()

        resultDataUpb = LivePagedListBuilder(upbDataSourceFactory, config).build()
        return resultDataUpb
    }

    fun requestDataDetailUpb(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String,
        idPermintaan: Int
    ): LiveData<HeaderUsulanPermintaanBarang>{
        val resultDetailUpb = MutableLiveData<HeaderUsulanPermintaanBarang>()
        compositeDisposable.add(apiService.requestDetailUpb(apiKey,token,idPermintaan)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                it.header
            }
            .subscribe(
                {
                    resultDetailUpb.postValue(it)
                }, {
                    Timber.e(it)
                }
            ))
        return resultDetailUpb
    }

    fun requestItemInDetailUpb(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String,
        idPermintaan: Int
    ): LiveData<List<ItemPermintaanBarang>>{
        val resultItemUpb = MutableLiveData<List<ItemPermintaanBarang>>()
        compositeDisposable.add(apiService.requestDetailUpb(apiKey, token, idPermintaan)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                it.data
            }.subscribe(
                {
                    resultItemUpb.postValue(it)
                }, {
                    Timber.e(it)
                }
            ))
        return resultItemUpb
    }

    fun getNetworkState() : LiveData<NetworkState>{
        return Transformations.switchMap(
            upbDataSourceFactory.usulanPermintaanLiveDataSource,
            UpbDataSource::networkState
        )
    }
}