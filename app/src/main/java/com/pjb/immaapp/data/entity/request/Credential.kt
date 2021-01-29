package com.pjb.immaapp.data.entity.request

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class Credential(
    @field:SerializedName("username")
    var username: String,
    @field:SerializedName("password")
    var password: String,
    @field:SerializedName("api_key")
    var apiKey: String
)