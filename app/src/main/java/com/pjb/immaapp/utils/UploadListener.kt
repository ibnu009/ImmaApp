package com.pjb.immaapp.utils

interface UploadListener {
    fun onSuccess(message: String)
    fun onError(message: String)
}