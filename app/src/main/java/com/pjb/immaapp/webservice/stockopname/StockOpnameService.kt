package com.pjb.immaapp.webservice.stockopname

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
        @Field("barcode")barcode: String
    ): Observable<ResponseStockOpname>
}