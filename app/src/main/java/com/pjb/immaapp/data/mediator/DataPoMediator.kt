package com.pjb.immaapp.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.pjb.immaapp.data.entity.local.po.PurchaseOrders
import com.pjb.immaapp.data.local.db.ImmaDatabase
import com.pjb.immaapp.utils.PoMapper
import com.pjb.immaapp.webservice.po.PurchaseOrderService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class DataPoMediator(
    private val database: ImmaDatabase,
    private val apiService: PurchaseOrderService,
    private val mapper: PoMapper,
    private val apiKey: String,
    private val token: String,
    private val keywords: String?
) : RxRemoteMediator<Int, PurchaseOrders.PurchaseOrderEntity>() {

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, PurchaseOrders.PurchaseOrderEntity>
    ): Single<MediatorResult> {
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
                            ?: throw InvalidObjectException("Data po is empty")

                        remoteKeys.prevKey ?: -1
                    }

                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                            ?: throw InvalidObjectException("Data po is empty")

                        remoteKeys.nextKey ?: -1
                    }
                }
            }
            .flatMap { page ->
                if (page == -1) {
                    Timber.d("Ended Early")
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                } else {
                    apiService.requestListPurchaseOrderSingle(apiKey, token, keywords)
                        .map { mapper.transform(it) }
                        .map {
                            Timber.d("CheckDatabase ${it.data}")
                            insertToDb(loadType, it)}
                        .map<MediatorResult> {
                            MediatorResult.Success(endOfPaginationReached = true)
                        }
                        .onErrorReturn { MediatorResult.Error(it) }
                }
            }
            .onErrorReturn { MediatorResult.Error(it) }
    }

    @Suppress("DEPRECATION")
    private fun insertToDb(loadType: LoadType, data: PurchaseOrders): PurchaseOrders {
        database.beginTransaction()
        try {
            if (loadType == LoadType.REFRESH) {
                database.getDataPoDao().clearDataPo()
            }
            database.getDataPoDao().insertPurchaseOrder(data.data)
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }
        return data
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, PurchaseOrders.PurchaseOrderEntity>): PurchaseOrders.PurchaseOrderKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { po ->
            database.getRemoteKeyDataPo().remoteKeysById(po.idPo)
        }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, PurchaseOrders.PurchaseOrderEntity>): PurchaseOrders.PurchaseOrderKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { po ->
            database.getRemoteKeyDataPo().remoteKeysById(po.idPo)
        }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PurchaseOrders.PurchaseOrderEntity>): PurchaseOrders.PurchaseOrderKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.idPo?.let { id ->
                database.getRemoteKeyDataPo().remoteKeysById(id)
            }
        }
    }
}