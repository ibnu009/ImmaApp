package com.pjb.immaapp.webservice.usulan

import com.pjb.immaapp.data.remote.response.ResponseCreateUpb
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UpbUploadService {
    @POST("api/fpb/create")
    @Multipart
    fun createPermintaanBarang(
        @Part token: MultipartBody.Part,
        @Part apiKey: MultipartBody.Part,
        @Part("requireddate") requiredDate: RequestBody,
        @Part("description") description: RequestBody,
        @Part("notes") notes: RequestBody?,
        @Part("critical")critical: RequestBody,
        @Part("id_sdm")idSdm: RequestBody,
        @Part file : MultipartBody.Part
    ): Call<ResponseCreateUpb>
}