package com.pjb.immaapp.handler

interface RabAddSupplierListener {
    fun onInitiating()
    fun onSuccess(message: String)
    fun onFailure(message: String)
}