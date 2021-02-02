package com.pjb.immaapp.data.remote.response

import com.pjb.immaapp.data.entity.PurchaseOrder

data class ResponsePo(
    var status: Int,
    var data: List<PurchaseOrder>,
    var message: String
)
