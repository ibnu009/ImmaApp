package com.pjb.immaapp.data.entity.notification

import com.google.gson.annotations.SerializedName

data class NotificationMessage(
    @field:SerializedName("title")
    val senderName: String,
    @field:SerializedName("body")
    val note: String
)