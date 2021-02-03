package com.pjb.immaapp.webservice.usulan

import com.pjb.immaapp.data.remote.response.ResponseDetailUpb
import com.pjb.immaapp.data.remote.response.ResponseUsulanPermintaan
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.*

interface UsulanPermintaanBarangService {

    @POST("api/fpb/list")
    @FormUrlEncoded
    fun requestListUsulanPermintaanBarang(
        @Field("api_key") apiKey: String,
        @Field("token") token: String,
        @Field("keywords") keywords: String?
    ): Observable<ResponseUsulanPermintaan>

    @POST("api/fpb/detail")
    @FormUrlEncoded
    fun requestDetailUpb(
        @Field("api_key") apiKey: String,
        @Field("token") token: String,
        @Field("id_permintaan") idPermintaan: Int
    ): Observable<ResponseDetailUpb>

}