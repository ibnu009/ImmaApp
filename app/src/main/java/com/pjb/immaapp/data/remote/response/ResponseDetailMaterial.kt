package com.pjb.immaapp.data.remote.response

import com.pjb.immaapp.data.entity.upb.Company
import com.pjb.immaapp.data.entity.upb.Material

data class ResponseDetailMaterial(
    var status: Int,
    var header: Material,
    var company: List<Company>
)
