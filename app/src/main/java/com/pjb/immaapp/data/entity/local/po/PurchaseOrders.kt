package com.pjb.immaapp.data.entity.local.po

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
data class PurchaseOrders(
    val total: Int = 0,
    val page: Int = 0,
    val data: List<PurchaseOrderEntity>
) : Parcelable{

    @Parcelize
    @Entity
    data class PurchaseOrderEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "po_id")
        var idPo: Int,
        @ColumnInfo(name = "ponum")
        var ponum: String,
        @ColumnInfo(name = "encode_ponum")
        var encodePonum: String,
        @ColumnInfo(name = "job_title")
        var jobTitle: String,
        @ColumnInfo(name = "order_date")
        var orderDate: String,
        @ColumnInfo(name = "anggaran")
        var anggaran: Int
    ): Parcelable

    @Parcelize
    @Entity(tableName = "po_remote_keys")
    data class PurchaseOrderKeys(
        @PrimaryKey val idPo: Int,
        val prevKey: Int?,
        val nextKey: Int?
    ): Parcelable

}
