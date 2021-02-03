package com.pjb.immaapp.data.remote.response

import com.pjb.immaapp.data.entity.po.HeaderPurchaseOrder
import com.pjb.immaapp.data.entity.po.ItemPurchaseOrder

data class ResponseDetailPo(
    val status: Int,
    var header: HeaderPurchaseOrder,
    var data: List<ItemPurchaseOrder>
)
