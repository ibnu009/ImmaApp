package com.pjb.immaapp.data.remote.response

import com.pjb.immaapp.data.entity.upb.CreateUpb

data class ResponseCreateUpb(
    var status: Int,
    var message: String?,
    var data: CreateUpb
)
