package com.pjb.immaapp.data.local.dao.supplier

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pjb.immaapp.data.entity.local.supplier.Suppliers

@Dao
interface SupplierRemoteKeys {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<Suppliers.SupplierKey>)
    
    @Query("SELECT * FROM supplier_remote_keys WHERE id = :id")
    fun remoteKeysById(id: Int): Suppliers.SupplierKey

    @Query("DELETE FROM supplier_remote_keys")
    fun clearRemoteKeys()
}