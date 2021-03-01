package com.pjb.immaapp.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @field:SerializedName("username")
    var username: String,
    @field:SerializedName("nama")
    var name: String,
    @field: SerializedName("id_sdm")
    var idSdm: String,
    @field:SerializedName("token")
    var token: String
): Parcelable
