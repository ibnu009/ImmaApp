package com.pjb.immaapp.data.entity

import com.google.gson.annotations.SerializedName

data class PermintaanBarang(
    var no: Int,
    @field:SerializedName("id_permintaan")
    var idPermintaan: Int,
    @field:SerializedName("no_permintaan")
    var noPermintaan: String,
    @field:SerializedName("tgl_permohonan")
    var tanggalPermohonan: String
)
