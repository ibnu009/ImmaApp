package com.pjb.immaapp.webservice.usulan

import com.pjb.immaapp.data.remote.response.ResponseUsulanPermintaan
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DetailUpbService {
    @POST("fpb/detail")
    @FormUrlEncoded
    fun requestDetailUpb(
        @Field("api_key") apiKey : String,
        @Field("token") token: String,
        @Field("id_permintaan") idPermintaan: String
    ): Observable<ResponseUsulanPermintaan>
}