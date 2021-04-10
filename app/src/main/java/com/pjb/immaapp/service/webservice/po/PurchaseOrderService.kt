package com.pjb.immaapp.service.webservice.po

import com.pjb.immaapp.data.remote.response.ResponseDetailPo
import com.pjb.immaapp.data.remote.response.ResponsePo
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PurchaseOrderService {

    @POST("/api/po/list")
    @FormUrlEncoded
    fun requestListPurchaseOrder(
        @Field("api_key") apiKey: String,
        @Field("token") token: String,
        @Field("keywords") keywords: String?
    ): Observable<ResponsePo>

    @POST("/api/po/list")
    @FormUrlEncoded
    fun requestListPurchaseOrderSingle(
        @Field("api_key") apiKey: String,
        @Field("token") token: String,
        @Field("keywords") keywords: String?
    ): Single<ResponsePo>

    @POST("/api/po/detail")
    @FormUrlEncoded
    fun requestDetailPurchaseOrder(
        @Field("api_key") apiKey: String,
        @Field("token") token: String,
        @Field("ponum") ponum: String
    ): Observable<ResponseDetailPo>

}