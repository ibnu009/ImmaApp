package com.pjb.immaapp.webservice.stockopname

import com.pjb.immaapp.data.remote.response.ResponseCreateStokOpname
import com.pjb.immaapp.data.remote.response.ResponseStockOpname
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface StockOpnameService {
    @POST("api/stock-opname/scanner")
    @FormUrlEncoded
    fun getStockOpnameResponse(
        @Field("api_key") apiKey: String,
        @Field("token")token: String,
        @Field("barcode")barcode: Int
    ): Observable<ResponseStockOpname>

    @POST("api/stock-opname/create")
    @FormUrlEncoded
    fun postStokOpname(
        @Field("api_key") apiKey: String,
        @Field("token") token: String,
        @Field("itemnum") itemNum: Int,
        @Field("notes") notes: String,
        @Field("stock") stock: Int,
        @Field("kondisi") kondisi: String
    ): Observable<ResponseCreateStokOpname>
}