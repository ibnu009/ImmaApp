package com.pjb.immaapp.data.remote.response

import com.pjb.immaapp.data.entity.upload.UploadUsulan

data class ResponseUploadUsulan(
    val status: Int,
    val message: String,
    val data: UploadUsulan
)