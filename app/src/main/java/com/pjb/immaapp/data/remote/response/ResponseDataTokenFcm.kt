package com.pjb.immaapp.data.remote.response

import com.pjb.immaapp.data.entity.notification.NotificationModel

data class ResponseDataTokenFcm(
    val status: Int,
    val data: List<NotificationModel>
)
