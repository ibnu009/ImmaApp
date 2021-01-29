package com.pjb.immaapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.pjb.immaapp.data.entity.User

data class ResponseUser(
    @field:SerializedName("data")
    var data: User,
    @field:SerializedName("status")
    var status: Int
)