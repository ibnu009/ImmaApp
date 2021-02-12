package com.pjb.immaapp.utils

import com.pjb.immaapp.data.entity.local.po.PurchaseOrders
import com.pjb.immaapp.data.entity.po.PurchaseOrder
import com.pjb.immaapp.data.remote.response.ResponsePo

class PoMapper {
    fun transform(response: ResponsePo): PurchaseOrders {
        return with(response) {
            PurchaseOrders(data = data.map {
                PurchaseOrders.PurchaseOrderEntity(
                    idPo = 0,
                    jobTitle = it.jobTitle,
                    ponum = it.ponum,
                    encodePonum = it.encodePonum,
                    anggaran = it.anggaran,
                    orderDate = it.orderDate
                )
            })
        }
    }
}