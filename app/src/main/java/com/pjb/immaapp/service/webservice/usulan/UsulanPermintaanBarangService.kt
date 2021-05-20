package com.pjb.immaapp.service.webservice.usulan

import com.pjb.immaapp.data.remote.response.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

interface UsulanPermintaanBarangService {

    @POST("/api/fpb/list")
    @FormUrlEncoded
    fun requestListUsulanPermintaanBarang(
        @Field("api_key") apiKey: String,
        @Field("token") token: String,
        @Field("keywords") keywords: String?
    ): Observable<ResponseUsulanPermintaan>

    @POST("/api/fpb/detail")
    @FormUrlEncoded
    fun requestDetailUpb(
        @Field("api_key") apiKey: String,
        @Field("token") token: String,
        @Field("id_permintaan") idPermintaan: Int
    ): Observable<ResponseDetailUpb>

    @POST("/api/fpb/list-material")
    @FormUrlEncoded
    fun requestListMaterial(
        @Field("api_key") apiKey: String,
        @Field("token") token: String,
        @Field("id_permintaan") idPermintaan: Int
    ): Observable<ResponseMaterial>

    @POST("/api/material/detail")
    @FormUrlEncoded
    fun requestDetailMaterial(
        @Field("api_key") apiKey: String,
        @Field("token") token: String,
        @Field("id_permintaan_detail") idDetail: Int
    ): Observable<ResponseDetailMaterial>

    @POST("/api/data/companies")
    @FormUrlEncoded
    fun requestDataSupplier(
        @Field("api_key") apiKey: String,
        @Field("token") token: String
    ): Observable<ResponseSupplier>

    @POST("/api/data/companies")
    @FormUrlEncoded
    fun requestDataSupplierSingle(
        @Field("api_key") apiKey: String,
        @Field("token") token: String
    ): Single<ResponseSupplier>

}