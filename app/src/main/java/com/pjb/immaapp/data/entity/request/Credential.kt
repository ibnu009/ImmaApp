package com.pjb.immaapp.data.entity.request

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class Credential(
    var username: String,
    var password: String,
    @field:SerializedName("api_key")
    var apiKey: String
)