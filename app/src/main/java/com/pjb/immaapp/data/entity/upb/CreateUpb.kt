package com.pjb.immaapp.data.entity.upb

import com.google.gson.annotations.SerializedName

data class CreateUpb(
    @field:SerializedName("no_permintaan")
    var noPermintaan: String,
    @field:SerializedName("file_name")
    var fileName: String,
    @field:SerializedName("file_url")
    var fileUrl: String
)
