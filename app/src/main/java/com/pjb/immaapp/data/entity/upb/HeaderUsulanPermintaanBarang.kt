package com.pjb.immaapp.data.entity.upb

import com.google.gson.annotations.SerializedName

data class HeaderUsulanPermintaanBarang(
    @field:SerializedName("tgl_permohonan")
    var tanggalPermohonan: String,
    @field:SerializedName("tgl_dibutuhkan")
    var tanggalDibutuhkan: String,
    @field:SerializedName("pemohon")
    var pemohon: String,
    @field:SerializedName("job_title")
    var jobTitle: String
)