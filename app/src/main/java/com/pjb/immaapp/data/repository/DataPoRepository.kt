package com.pjb.immaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.pjb.immaapp.data.entity.po.HeaderPurchaseOrder
import com.pjb.immaapp.data.entity.po.ItemPurchaseOrder
import com.pjb.immaapp.data.entity.po.PurchaseOrder
import com.pjb.immaapp.data.source.po.PoDataSource
import com.pjb.immaapp.data.source.po.PoDataSourceFactory
import com.pjb.immaapp.data.source.po.PoItemDataSource
import com.pjb.immaapp.data.source.po.PoItemDataSourceFactory
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
    private lateinit var poItemDataSourceFactory: PoItemDataSourceFactory
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

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
        lateinit var resultDataPo: LiveData<PagedList<PurchaseOrder>>
        poDataSourceFactory = PoDataSourceFactory(apiService, compositeDisposable, token, keywords)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(ITEM_PER_PAGE)
            .setPageSize(ITEM_PER_PAGE)
            .build()

        resultDataPo = LivePagedListBuilder(poDataSourceFactory, config).build()
        return resultDataPo
    }

    fun requestDetailDataPo(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String,
        ponum: String
    ): LiveData<HeaderPurchaseOrder> {
        val resultDetailPo = MutableLiveData<HeaderPurchaseOrder>()
        compositeDisposable.add(apiService.requestDetailPurchaseOrder(apiKey, token, ponum)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    if (it.status == 200) {
                        resultDetailPo.postValue(it.header)
                        networkState.postValue(NetworkState.LOADED)
                        Timber.d("Complete data Header Po with PoNum: $ponum, data is ${it.header.vendor}")
                    } else {
                        networkState.postValue(NetworkState.ERROR)
                        Timber.e("Error 400")
                    }
                }, {
                    Timber.e("Error : $it")
                }
            ))

        return resultDetailPo
    }

    fun requestItemInDetailDataPo(
        compositeDisposable: CompositeDisposable,
        token: String,
        ponum: String
    ): LiveData<PagedList<ItemPurchaseOrder>> {
        lateinit var resultItemPo: LiveData<PagedList<ItemPurchaseOrder>>
        poItemDataSourceFactory =
            PoItemDataSourceFactory(apiService, compositeDisposable, token, ponum)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ITEM_PER_PAGE)
            .build()

        resultItemPo = LivePagedListBuilder(poItemDataSourceFactory, config).build()
        return resultItemPo
    }

    fun getNetWorkState(): LiveData<NetworkState> {
        return Transformations.switchMap(
            poDataSourceFactory.poLiveDataSource,
            PoDataSource::networkState
        )
    }

    fun getPoItemNetworkSate(): LiveData<NetworkState> {
        return Transformations.switchMap(
            poItemDataSourceFactory.poLiveDataSource,
            PoItemDataSource::networkState
        )
    }
}