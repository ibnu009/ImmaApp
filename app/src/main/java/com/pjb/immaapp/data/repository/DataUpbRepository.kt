package com.pjb.immaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.*
import com.pjb.immaapp.data.entity.upb.*
import com.pjb.immaapp.data.source.usulanpermintaan.*
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.NetworkState.Companion.ERROR
import com.pjb.immaapp.utils.NetworkState.Companion.LOADED
import com.pjb.immaapp.utils.global.ImmaEventHandler
import com.pjb.immaapp.service.webservice.RetrofitApp
import com.pjb.immaapp.service.webservice.RetrofitApp.Companion.ITEM_PER_PAGE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import timber.log.Timber

class DataUpbRepository {
    private val apiService = RetrofitApp.getUpbService()
    private val uploadService = RetrofitApp.getUploadService()
    private lateinit var upbDataSourceFactory: UpbDataSourceFactory
    private lateinit var upbItemDataSourceFactory: UpbItemDataSourceFactory
    private lateinit var supplierDataSourceFactory: SupplierDataSourceFactory

    val networkState: ImmaEventHandler<NetworkState> = ImmaEventHandler()

    companion object {
        @Volatile
        private var instance: DataUpbRepository? = null
        fun getInstance(): DataUpbRepository =
            instance ?: synchronized(this) {
                instance ?: DataUpbRepository()
            }
    }

//    Module : Usulan Permintaan Barang

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

//    Module Detail Usulan Permintaan Barang
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

    //    Module Detail Usulan Permintaan Barang - bagian material
    fun requestItemInDetailDataUpb(
        compositeDisposable: CompositeDisposable,
        token: String,
        idPermintaan: Int
    ): LiveData<PagedList<ItemPermintaanBarang>> {
        lateinit var resultItemUpb: LiveData<PagedList<ItemPermintaanBarang>>
        upbItemDataSourceFactory =
            UpbItemDataSourceFactory(apiService, compositeDisposable, token, idPermintaan)

        val config = PagedList.Config.Builder()
            .setPageSize(ITEM_PER_PAGE)
            .setEnablePlaceholders(false)
            .build()

        resultItemUpb = LivePagedListBuilder(upbItemDataSourceFactory, config).build()
        return resultItemUpb
    }

    fun requestMaterialDetail(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String,
        idDetailMaterial: Int
    ): LiveData<Material> {
        networkState.postValue(NetworkState.LOADING)
        val resultDetailMaterial = MutableLiveData<Material>()

        compositeDisposable.add(apiService.requestDetailMaterial(apiKey, token, idDetailMaterial)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { it.header }
            .subscribe(
                {
                    resultDetailMaterial.postValue(it)
                    networkState.postValue(LOADED)
                }, {
                    networkState.postValue(ERROR)
                    Timber.e(it)
                }
            ))
        return resultDetailMaterial
    }

    //    Company list muncul pada bagian detail material
    fun requestCompanyList(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String,
        idDetailMaterial: Int
    ): LiveData<List<Company>> {
        val resultCompanyList = MutableLiveData<List<Company>>()
        compositeDisposable.add(apiService.requestDetailMaterial(apiKey, token, idDetailMaterial)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { it.company }
            .subscribe(
                {
                    resultCompanyList.postValue(it)
                    networkState.postValue(LOADED)
                }, {
                    networkState.postValue(ERROR)
                    Timber.e(it)
                }
            ))
        return resultCompanyList
    }

    fun requestListDataSupplier(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String
    ): LiveData<PagedList<Supplier>> {
        lateinit var resultDataSupplier: LiveData<PagedList<Supplier>>

        supplierDataSourceFactory = SupplierDataSourceFactory(apiService, compositeDisposable, token)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ITEM_PER_PAGE)
            .build()
        resultDataSupplier = LivePagedListBuilder(supplierDataSourceFactory, config).build()
        return resultDataSupplier
    }

    fun uploadUsulanPermintaanWithoutFile(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String
    ) {

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