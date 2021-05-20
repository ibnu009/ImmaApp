package com.pjb.immaapp.data.entity.local.supplier

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Suppliers(
    val data: List<SupplierEntity>
): Parcelable{

    @Parcelize
    @Entity
    data class SupplierEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id_supplier")
        var id_supplier: Int,
        @ColumnInfo(name = "id")
        var id: Int,
        @ColumnInfo(name = "nama")
        var nama: String
    ) : Parcelable

    @Parcelize
    @Entity(tableName = "supplier_remote_keys")
    data class SupplierKey(
        @PrimaryKey val id: Int,
        val prevKey: Int?,
        val nextKey: Int?
    ): Parcelable
}


