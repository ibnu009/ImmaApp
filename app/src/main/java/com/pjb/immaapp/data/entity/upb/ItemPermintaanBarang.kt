package com.pjb.immaapp.data.entity.upb

import com.google.gson.annotations.SerializedName

data class ItemPermintaanBarang(
    var no: Int,
    @field:SerializedName("id_detail")
    var idDetai: Int,
    @field:SerializedName("itemnum")
    var itemNum: Int,
    @field:SerializedName("name")
    var barang: String,
    @field:SerializedName("qty")
    var quantity: Int,
    @field:SerializedName("img_url")
    var imgPath: String?
)
