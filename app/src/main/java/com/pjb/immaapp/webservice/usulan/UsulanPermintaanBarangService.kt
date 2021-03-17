package com.pjb.immaapp.webservice.usulan

import com.pjb.immaapp.data.entity.upb.ItemPermintaanBarang
import com.pjb.immaapp.data.remote.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*
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

    @POST("api/fpb/list-material")
    @FormUrlEncoded
    fun requestListMaterial(
        @Field("api_key") apiKey: String,
        @Field("token") token: String,
        @Field("id_permintaan") idPermintaan: Int
    ): Observable<ResponseMaterial>

    @POST("api/material/detail")
    @FormUrlEncoded
    fun requestDetailMaterial(
        @Field("api_key") apiKey: String,
        @Field("token") token: String,
        @Field("id_permintaan_detail") idDetail: Int
    ): Observable<ResponseDetailMaterial>

}