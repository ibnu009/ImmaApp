package com.pjb.immaapp.data.entity.po

import com.google.gson.annotations.SerializedName

data class ItemPurchaseOrder(
    @field:SerializedName("nama_material")
    var namaMaterial: String,
    @field:SerializedName("unitcost")
    var unitCost: Int,
    @field:SerializedName("qty")
    var quantity: Int
)