package com.pjb.immaapp.utils

interface UploadUsulanListener {
    fun onSuccess(message: String, idPermintaan: Int)
    fun onError(message: String)
}