package com.pjb.immaapp.ui.notification

interface NotificationListener {
    fun onInitiating()
    fun onLoaded()
    fun onOpenNotification()
    fun onErrorOccurred(message: String)
}