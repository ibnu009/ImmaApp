package com.pjb.immaapp.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.pjb.immaapp.data.entity.local.supplier.Suppliers
import com.pjb.immaapp.data.local.db.ImmaDatabase
import com.pjb.immaapp.service.webservice.usulan.UsulanPermintaanBarangService
import com.pjb.immaapp.utils.mapper.SupplierMapper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class SupplierMediator(
    private val database: ImmaDatabase,
    private val apiService: UsulanPermintaanBarangService,
    private val mapper: SupplierMapper,
    private val apiKey: String,
    private val token: String,
) : RxRemoteMediator<Int, Suppliers.SupplierEntity>() {

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, Suppliers.SupplierEntity>
    ): Single<MediatorResult> {
        Timber.d("Call SupplierMediator")
        Timber.d("LoadType is $loadType with State is $state")
        return Single.just(loadType)
            .subscribeOn(Schedulers.io())
            .map {
                when (it) {
                    LoadType.REFRESH -> {
                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                        remoteKeys?.nextKey?.minus(1) ?: 1
                    }
                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)
                            ?: throw InvalidObjectException("Data supplier is empty")

                        remoteKeys.prevKey ?: -1
                    }
                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                            ?: throw InvalidObjectException("Data supplier is empty")
                        remoteKeys.nextKey ?: -1
                    }
                }
            }.flatMap { page ->
                if (page == -1) {
                    Timber.d("Ended Early")
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                } else {
                    apiService.requestDataSupplierSingle(apiKey, token)
                        .map {
                            mapper.transform(it)
                        }
                        .map {
                            Timber.d("Check data ${it.data}")
                            insertToDB(loadType, it)
                        }
                        .map<MediatorResult> {
                            MediatorResult.Success(endOfPaginationReached = true)
                        }
                        .onErrorReturn {
                            MediatorResult.Error(it)
                        }
                }
            }
            .onErrorReturn {
                Timber.e("error is : ${it.message}")
                MediatorResult.Error(it)
            }.doOnError {
                Timber.d("error is : ${it.cause}")
                MediatorResult.Error(it)
            }
    }

    @Suppress("DEPRECATION")
    private fun insertToDB(loadType: LoadType, data: Suppliers): Suppliers {
        Timber.d("Check loadType : ${loadType.name}")
        database.beginTransaction()
        try {
            if (loadType == LoadType.REFRESH) {
                database.getSupplierDao().clearSuppliers()
            }
            database.getSupplierDao().insertSupplier(data.data)
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }
        return data
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, Suppliers.SupplierEntity>): Suppliers.SupplierKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { supplier ->
                database.getRemoteKeySupplier().remoteKeysById(supplier.id)
            }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Suppliers.SupplierEntity>): Suppliers.SupplierKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { supplier ->
                database.getRemoteKeySupplier().remoteKeysById(supplier.id)
            }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Suppliers.SupplierEntity>): Suppliers.SupplierKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.getRemoteKeySupplier().remoteKeysById(id)
            }
        }
    }

}