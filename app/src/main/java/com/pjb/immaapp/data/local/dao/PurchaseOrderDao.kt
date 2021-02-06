package com.pjb.immaapp.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pjb.immaapp.data.entity.local.po.PurchaseOrders

@Dao
interface PurchaseOrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPurchaseOrder(listPo: List<PurchaseOrders.PurchaseOrderEntity>)

    @Query("SELECT * FROM purchaseorderentity")
    fun getAllDataPo(): DataSource.Factory<Int, PurchaseOrders.PurchaseOrderEntity>

    @Query("DELETE FROM purchaseorderentity")
    fun clearDataPo()

}