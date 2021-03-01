package com.pjb.immaapp.handler

interface UpbFileUploadListener {
    fun onInitiating()
    fun onSuccess(message: String, idPermintaan: Int?)
    fun onFailure(message: String)
}