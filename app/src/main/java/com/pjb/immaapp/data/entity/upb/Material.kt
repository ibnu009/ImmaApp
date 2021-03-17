package com.pjb.immaapp.data.entity.upb

import com.google.gson.annotations.SerializedName

data class Material(
    @field:SerializedName("id_permintaan_detail")
    var idPermintaanDetail: Int,
    var material: String,
    var qty: Int,
    @field:SerializedName("avgcost")
    var averageCost: Int,
    @field:SerializedName("lastpo")
    var lastPo: String,
    var rab: Int
)