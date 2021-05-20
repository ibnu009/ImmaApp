package com.pjb.immaapp.data.entity.local.fts

import androidx.room.Entity
import androidx.room.Fts4
import com.pjb.immaapp.data.entity.local.supplier.Suppliers

@Entity(tableName = "supplier_fts")
@Fts4(contentEntity = Suppliers.SupplierEntity::class)
data class SupplierFTS(
    var nama: String
)
