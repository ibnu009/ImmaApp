package com.pjb.immaapp.data.remote.response

import com.pjb.immaapp.data.entity.Karyawan

data class ResponseKaryawan(
    val status: Int,
    val data: List<Karyawan>
)
