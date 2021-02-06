package com.pjb.immaapp.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.pjb.immaapp.data.entity.local.po.PurchaseOrders
import com.pjb.immaapp.data.local.db.ImmaDatabase
import com.pjb.immaapp.webservice.po.PurchaseOrderService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.InvalidObjectException

@ExperimentalPagingApi
class DataPoMediator(
    private val database: ImmaDatabase,
    private val apiService: PurchaseOrderService,
    private val apiKey: String,
    private val token: String,
    private val keywords: String?
) : RxRemoteMediator<Int, PurchaseOrders.PurchaseOrderEntity>() {
//
//    override fun loadSingle(
//        loadType: LoadType,
//        state: PagingState<Int, PurchaseOrders.PurchaseOrderEntity>
//    ): Single<MediatorResult> {
//        return Single.just(loadType)
//            .subscribeOn(Schedulers.io())
//            .map {
//                when (it) {
//                    LoadType.REFRESH -> {
//                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                        remoteKeys?.nextKey?.minus(1) ?: 1
//                    }
//                    LoadType.PREPEND -> {
//                        val remoteKeys = getRemoteKeyForFirstItem(state)
//                            ?: throw InvalidObjectException("Data po is empty")
//
//                        remoteKeys.prevKey ?: -1
//                    }
//
//                    LoadType.APPEND -> {
//                        val remoteKeys = getRemoteKeyForLastItem(state)
//                            ?: throw InvalidObjectException("Data po is empty")
//
//                        remoteKeys.nextKey ?: -1
//                    }
//                }
//            }
//            .flatMap { page ->
//                if (page == -1) {
//                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
//                } else {
//                    apiService.requestListPurchaseOrderSingle(apiKey, token, keywords)
//                        .map {
//
//                        }
//                        .map {
//
//                        }
//                }
//            }
//    }

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

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, PurchaseOrders.PurchaseOrderEntity>
    ): Single<MediatorResult> {
        TODO("Not yet implemented")
    }

}