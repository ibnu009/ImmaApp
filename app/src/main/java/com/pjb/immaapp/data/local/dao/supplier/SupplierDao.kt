package com.pjb.immaapp.data.local.dao.supplier

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.pjb.immaapp.data.entity.local.supplier.Suppliers

@Dao
interface SupplierDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSupplier(supplier: List<Suppliers.SupplierEntity>)

    @Query("SELECT * FROM supplierentity ORDER BY id ASC")
    fun getAllSuppliers(): PagingSource<Int, Suppliers.SupplierEntity>

    @RawQuery(observedEntities = [Suppliers.SupplierEntity::class])
    fun getAllSuppliersQuery(query: SupportSQLiteQuery): PagingSource<Int, Suppliers.SupplierEntity>

    @Query("DELETE FROM supplierentity")
    fun clearSuppliers()


}