package com.pjb.immaapp.service.webservice.upload

import com.pjb.immaapp.data.remote.response.ResponseUpload
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadService {

    @Multipart
    @POST("/api/fpb/create")
    fun uploadUsulan(
        @Part("token") token: RequestBody,
        @Part("api_key") apiKey: RequestBody,
        @Part("requireddate") requiredDate: RequestBody,
        @Part("description") description: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part("critical") critical: RequestBody,
        @Part("id_sdm") idSdm: RequestBody,
        @Part("file") file: MultipartBody.Part
    ): Observable<ResponseUpload>

    @Multipart
    @POST("/api/fpb/create-material")
    fun uploadMaterial(
        @Part("token") token: RequestBody,
        @Part("api_key") apiKey: RequestBody,
        @Part("itemnum") itemNum: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part("qty") qty: RequestBody,
        @Part("id_permintaan") idPermintaan: RequestBody,
        @Part("linetype") lineType: RequestBody,
        @Part("img") path: MultipartBody.Part
    ): Observable<ResponseUpload>

    @Multipart
    @POST("/api/rab/add-company")
    fun uploadSupplier(
        @Part("token") token: RequestBody,
        @Part("api_key") apiKey: RequestBody,
        @Part("vendor") vendor: RequestBody,
        @Part("id_detail") idDetail: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("file") file: MultipartBody.Part
    ): Observable<ResponseUpload>

}