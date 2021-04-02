package com.pjb.immaapp.data.remote.response

import com.pjb.immaapp.data.entity.upb.Supplier

data class ResponseSupplier(
    var status: Int,
    var data: List<Supplier>
)