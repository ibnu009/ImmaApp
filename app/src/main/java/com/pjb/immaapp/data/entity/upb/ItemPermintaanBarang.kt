package com.pjb.immaapp.data.entity.upb

import com.google.gson.annotations.SerializedName

data class ItemPermintaanBarang(
    @field:SerializedName("itemnun")
    var itemNum: Int,
    @field:SerializedName("barang")
    var barang: String,
    @field:SerializedName("qty")
    var quantity: Int
)
