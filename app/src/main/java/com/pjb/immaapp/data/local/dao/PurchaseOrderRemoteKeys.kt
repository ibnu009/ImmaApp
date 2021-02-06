package com.pjb.immaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pjb.immaapp.data.entity.local.po.PurchaseOrders

@Dao
interface PurchaseOrderRemoteKeys {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<PurchaseOrders.PurchaseOrderKeys>)
    
    @Query("SELECT * FROM po_remote_keys WHERE idPo = :idPo")
    fun remoteKeysById(idPo: Int): PurchaseOrders.PurchaseOrderKeys

    @Query("DELETE FROM po_remote_keys")
    fun clearRemoteKeys()
}