package com.pjb.immaapp.data.repository

import android.content.Context
import android.net.Uri
import android.os.FileUtils
import android.text.TextUtils
import androidx.core.text.TextUtilsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.*
import com.pjb.immaapp.data.entity.upb.HeaderUsulanPermintaanBarang
import com.pjb.immaapp.data.entity.upb.ItemPermintaanBarang
import com.pjb.immaapp.data.entity.upb.PermintaanBarang
import com.pjb.immaapp.data.source.usulanpermintaan.UpbDataSource
import com.pjb.immaapp.data.source.usulanpermintaan.UpbDataSourceFactory
import com.pjb.immaapp.data.source.usulanpermintaan.UpbItemDataSource
import com.pjb.immaapp.data.source.usulanpermintaan.UpbItemDataSourceFactory
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.NetworkState.Companion.ERROR
import com.pjb.immaapp.utils.NetworkState.Companion.LOADED
import com.pjb.immaapp.utils.URIPathHelper
import com.pjb.immaapp.webservice.RetrofitApp
import com.pjb.immaapp.webservice.RetrofitApp.Companion.ITEM_PER_PAGE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File

class DataUpbRepository {
    private val apiService = RetrofitApp.getUpbService()
    private val apiUploadService = RetrofitApp.getUploadUpbService()
    private lateinit var upbDataSourceFactory: UpbDataSourceFactory
    private lateinit var upbItemDataSourceFactory: UpbItemDataSourceFactory
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    private var disposable: Disposable? = null

    companion object {
        @Volatile
        private var instance: DataUpbRepository? = null
        fun getInstance(): DataUpbRepository =
            instance ?: synchronized(this) {
                instance ?: DataUpbRepository()
            }
    }

    fun requestDataListUpb(
        compositeDisposable: CompositeDisposable,
        token: String,
        keywords: String?
    ): LiveData<PagedList<PermintaanBarang>> {

        lateinit var resultDataUpb: LiveData<PagedList<PermintaanBarang>>

        upbDataSourceFactory =
            UpbDataSourceFactory(apiService, compositeDisposable, token, keywords)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ITEM_PER_PAGE)
            .build()

        resultDataUpb = LivePagedListBuilder(upbDataSourceFactory, config).build()

        return resultDataUpb
    }

    fun requestDataDetailDataUpb(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String,
        idPermintaan: Int
    ): LiveData<HeaderUsulanPermintaanBarang> {
        networkState.postValue(NetworkState.LOADING)

        val resultDetailUpb = MutableLiveData<HeaderUsulanPermintaanBarang>()
        compositeDisposable.add(apiService.requestDetailUpb(apiKey, token, idPermintaan)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                it.header
            }
            .subscribe(
                {
                    resultDetailUpb.postValue(it)
                    networkState.postValue(LOADED)
                }, {
                    Timber.e(it)
                    networkState.postValue(ERROR)
                }
            ))
        return resultDetailUpb
    }

    fun requestItemInDetailDataUpb(
        compositeDisposable: CompositeDisposable,
        token: String,
        idPermintaan: Int
    ): LiveData<PagedList<ItemPermintaanBarang>> {
        lateinit var resultItemUpb : LiveData<PagedList<ItemPermintaanBarang>>
        upbItemDataSourceFactory =
            UpbItemDataSourceFactory(apiService, compositeDisposable, token, idPermintaan)

        val config = PagedList.Config.Builder()
            .setPageSize(ITEM_PER_PAGE)
            .setEnablePlaceholders(false)
            .build()

        resultItemUpb = LivePagedListBuilder(upbItemDataSourceFactory, config).build()
        return resultItemUpb
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap(
            upbDataSourceFactory.upbLiveDataSource,
            UpbDataSource::networkState
        )
    }

    fun getUpbItemNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap(
            upbItemDataSourceFactory.upbLiveDataSource,
            UpbItemDataSource::networkState
        )
    }

}