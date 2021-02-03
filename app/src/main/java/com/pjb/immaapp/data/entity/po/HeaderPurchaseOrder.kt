package com.pjb.immaapp.data.entity.po

import com.google.gson.annotations.SerializedName

data class HeaderPurchaseOrder(
    @field:SerializedName("tgl_order")
    var tanggalOrder: String,
    @field:SerializedName("anggaran")
    var anggaran: Int,
    @field:SerializedName("jobtitle")
    var jobTitle: String,
    @field:SerializedName("vendor")
    var vendor: String,
    @field:SerializedName("levering")
    var levering: String
)
