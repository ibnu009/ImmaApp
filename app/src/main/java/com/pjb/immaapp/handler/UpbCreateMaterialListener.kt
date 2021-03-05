package com.pjb.immaapp.handler

interface UpbCreateMaterialListener {
    fun onInitiating()
    fun onSuccess(message: String)
    fun onFailure(message: String)
}