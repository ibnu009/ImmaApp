package com.pjb.immaapp.data.remote.response

import com.pjb.immaapp.data.entity.upb.PermintaanBarang

data class ResponseUsulanPermintaan (
    var status : Int,
    var data : List<PermintaanBarang>,
    var message : String
)