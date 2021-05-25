package com.pjb.immaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.*
import androidx.paging.rxjava2.flowable
import com.pjb.immaapp.data.entity.Karyawan
import com.pjb.immaapp.data.entity.local.supplier.Suppliers
import com.pjb.immaapp.data.entity.upb.*
import com.pjb.immaapp.data.local.db.ImmaDatabase
import com.pjb.immaapp.data.mediator.SupplierMediator
import com.pjb.immaapp.data.remote.response.ResponseKaryawan
import com.pjb.immaapp.data.remote.response.ResponseSaveRab
import com.pjb.immaapp.data.source.usulanpermintaan.*
import com.pjb.immaapp.service.webservice.RetrofitApp
import com.pjb.immaapp.service.webservice.RetrofitApp.Companion.ITEM_PER_PAGE
import com.pjb.immaapp.utils.ConverterHelper
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.NetworkState.Companion.ERROR
import com.pjb.immaapp.utils.NetworkState.Companion.LOADED
import com.pjb.immaapp.utils.NetworkState.Companion.LOADING
import com.pjb.immaapp.utils.UploadListener
import com.pjb.immaapp.utils.UploadUsulanListener
import com.pjb.immaapp.utils.global.ImmaEventHandler
import com.pjb.immaapp.utils.mapper.SupplierMapper
import com.pjb.immaapp.utils.rq.SearchQuery
import com.pjb.immaapp.utils.utilsentity.GeneralErrorHandler
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import timber.log.Timber


class DataUpbRepository(
    private val database: ImmaDatabase
) {
    private val apiService = RetrofitApp.getUpbService()
    private val uploadService = RetrofitApp.getUploadService()
    private lateinit var upbDataSourceFactory: UpbDataSourceFactory
    private lateinit var upbItemDataSourceFactory: UpbItemDataSourceFactory
    private lateinit var supplierDataSourceFactory: SupplierDataSourceFactory

    private lateinit var mediator: SupplierMediator

    val networkState: ImmaEventHandler<NetworkState> = ImmaEventHandler()

    companion object {
        @Volatile
        private var instance: DataUpbRepository? = null
        fun getInstance(database: ImmaDatabase): DataUpbRepository =
            instance ?: synchronized(this) {
                instance ?: DataUpbRepository(database = database)
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
                    val error = GeneralErrorHandler().getError(it)
                    networkState.postValue(error)
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
                    val error = GeneralErrorHandler().getError(it)
                    networkState.postValue(error)
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
                    Timber.e("Cek error : $it")
                    val error = GeneralErrorHandler().getError(it)
                    networkState.postValue(error)
                }
            ))
        return resultCompanyList
    }

    @ExperimentalPagingApi
    fun requestListDataSupplier(
        apiKey: String,
        token: String
    ): Flowable<PagingData<Suppliers.SupplierEntity>> {
//        lateinit var resultDataSupplier: LiveData<PagedList<Supplier>>
        mediator = SupplierMediator(database, apiService, SupplierMapper(), apiKey, token)
        Timber.d("mediator is $mediator")

        return Pager(
            config = PagingConfig(
                pageSize = 18,
                enablePlaceholders = true,
            ),
            remoteMediator = mediator,
            pagingSourceFactory = {
                database.getSupplierDao().getAllSuppliers()
            },
        ).flowable

//        supplierDataSourceFactory = SupplierDataSourceFactory(
//            apiService,
//            compositeDisposable,
//            token
//        )
//
//        val config = PagedList.Config.Builder()
//            .setEnablePlaceholders(false)
//            .setPageSize(ITEM_PER_PAGE)
//            .build()
//        resultDataSupplier = LivePagedListBuilder(supplierDataSourceFactory, config).build()
//        return resultDataSupplier


    }

    fun getSearchedSupplier(
        token: String,
        keywords: String?
    ): Flowable<PagingData<Suppliers.SupplierEntity>> {
        val query = keywords?.let { SearchQuery.getSearchQuerySupplier(it) }
        return Pager(
            config = PagingConfig(20, enablePlaceholders = true),
            pagingSourceFactory = { database.getSupplierDao().getAllSuppliersQuery(query!!) }
        ).flowable
    }

    fun uploadUsulanPermintaanWithoutFile(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String,
        requireDate: String,
        description: String,
        notes: String,
        critical: String,
        idSdm: String,
        status: UploadUsulanListener
    ) {

        val mToken = token.toRequestBody(MultipartBody.FORM)
        val mApiKey = apiKey.toRequestBody(MultipartBody.FORM)
        val mRequireDate = requireDate.toRequestBody(MultipartBody.FORM)
        val mDescription = description.toRequestBody(MultipartBody.FORM)
        val mNotes = notes.toRequestBody(MultipartBody.FORM)
        val mCritical = critical.toRequestBody(MultipartBody.FORM)
        val mIdSdm = idSdm.toRequestBody(MultipartBody.FORM)

        val fileBody = "".toRequestBody("text/plain".toMediaTypeOrNull())
        val mFile = MultipartBody.Part.createFormData("file", "", fileBody)

        compositeDisposable.add(
            uploadService.uploadUsulan(
                token = mToken,
                apiKey = mApiKey,
                requiredDate = mRequireDate,
                description = mDescription,
                notes = mNotes,
                critical = mCritical,
                idSdm = mIdSdm,
                file = mFile
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Timber.d("test idPermintaan = ${it.data.idPermintaan}")
                    status.onSuccess(it.message, it.data.idPermintaan)
                }, {
                    val message = ConverterHelper().convertExceptionToMessage(it)
                    status.onError(message)
                })
        )
    }

    fun uploadMaterialWithoutFile(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String,
        vendor: String,
        idDetail: String,
        harga: String,
        status: UploadListener
    ) {

        val mToken = token.toRequestBody(MultipartBody.FORM)
        val mApiKey = apiKey.toRequestBody(MultipartBody.FORM)
        val mVendor = vendor.toRequestBody(MultipartBody.FORM)
        val mIdDetail = idDetail.toRequestBody(MultipartBody.FORM)
        val mHarga = harga.toRequestBody(MultipartBody.FORM)

        val fileBody = "".toRequestBody("text/plain".toMediaTypeOrNull())
        val mFile = MultipartBody.Part.createFormData("file", "", fileBody)

        compositeDisposable.add(
            uploadService.uploadSupplier(
                token = mToken,
                apiKey = mApiKey,
                vendor = mVendor,
                idDetail = mIdDetail,
                harga = mHarga,
                file = mFile
            ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe({
                    status.onSuccess(it.message)
                }, {
                    val message = ConverterHelper().convertExceptionToMessage(it)
                    status.onError(message)
                })
        )
    }

    fun saveRab(
        apiKey: String,
        token: String,
        idPermintaan: Int,
        idSdmApproval: Int,
        notes: String
    ): Single<ResponseSaveRab> {
        return apiService.saveRab(
            apiKey = apiKey,
            token = token,
            idPermintaan = idPermintaan,
            idSdmApproval = idSdmApproval,
            note = notes
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    }

    fun getListKaryawan(apiKey: String, token: String): Observable<ResponseKaryawan> {
        return apiService.getListKaryawan(apiKey, token)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

//    fun getSupplierNetworkState(): LiveData<NetworkState> {
//        return Transformations.switchMap(
//            supplierDataSourceFactory.supplierLiveDataSource,
//            SupplierDataSource::networkState
//        )
//    }

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