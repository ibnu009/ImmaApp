package com.pjb.immaapp.data.entity

import com.google.gson.annotations.SerializedName

data class Karyawan(
    @field:SerializedName("id_sdm")
    val idSdm: Int,
    val nama: String
)
