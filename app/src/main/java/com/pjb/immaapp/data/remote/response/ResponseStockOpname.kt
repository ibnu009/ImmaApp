package com.pjb.immaapp.data.remote.response

import com.pjb.immaapp.data.entity.stockopname.StockOpname

data class ResponseStockOpname(
    var status: Int,
    var data: List<StockOpname>
)
