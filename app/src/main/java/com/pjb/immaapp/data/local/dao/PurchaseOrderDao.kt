package com.pjb.immaapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.pjb.immaapp.data.entity.local.po.PurchaseOrders

@Dao
interface PurchaseOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPurchaseOrder(listPo: List<PurchaseOrders.PurchaseOrderEntity>)

    @Query("SELECT * FROM purchaseorderentity ORDER BY po_id ASC")
    fun getAllDataPo(): PagingSource<Int, PurchaseOrders.PurchaseOrderEntity>
    
    @RawQuery(observedEntities = [PurchaseOrders.PurchaseOrderEntity::class])
    fun getAllDataPoQuery(query: SupportSQLiteQuery): PagingSource<Int, PurchaseOrders.PurchaseOrderEntity>

    @Query("DELETE FROM purchaseorderentity")
    fun clearDataPo()

}