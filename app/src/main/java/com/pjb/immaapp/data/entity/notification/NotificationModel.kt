package com.pjb.immaapp.data.entity.notification

import com.google.gson.annotations.SerializedName

data class NotificationModel(
    @field:SerializedName("id_user")
    val idUser: Int,
    val nama: String,
    @field:SerializedName("token_fcm")
    val TokenFcm: String
)
