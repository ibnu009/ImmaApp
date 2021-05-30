package com.pjb.immaapp.ui.usulanpermintaanbarang.approval

interface ApprovalRabListener {
    fun onInitiating()
    fun onSuccess(message: String)
    fun onFailure(message: String)
}