package com.pjb.immaapp.data.entity

import com.google.gson.annotations.SerializedName

data class PurchaseOrder(
    var ponum: String,
    @field:SerializedName("encode_ponum")
    var encodePonum: String,
    @field:SerializedName("jobtitle")
    var jobTitle: String,
    @field:SerializedName("orderdate")
    var orderDate: String,
    var anggaran: Int
)
