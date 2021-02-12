package com.pjb.immaapp.data.entity.po.fts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import com.pjb.immaapp.data.entity.local.po.PurchaseOrders

@Entity(tableName = "data_po_fts")
@Fts4(contentEntity = PurchaseOrders.PurchaseOrderEntity::class)
data class DataPoFTS(
    @ColumnInfo(name = "job_title")
    var jobTitle: String,
    @ColumnInfo(name = "ponum")
    var ponum: String
)
