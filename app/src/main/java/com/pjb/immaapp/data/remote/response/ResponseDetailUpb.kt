package com.pjb.immaapp.data.remote.response

import com.pjb.immaapp.data.entity.upb.HeaderUsulanPermintaanBarang
import com.pjb.immaapp.data.entity.upb.ItemPermintaanBarang

data class ResponseDetailUpb(
    val status: Int,
    val header: HeaderUsulanPermintaanBarang,
    val data: List<ItemPermintaanBarang>
)