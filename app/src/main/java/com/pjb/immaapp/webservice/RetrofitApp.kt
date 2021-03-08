package com.pjb.immaapp.webservice

import com.pjb.immaapp.utils.network.NetworkInterceptor
import com.pjb.immaapp.webservice.login.LoginService
import com.pjb.immaapp.webservice.po.PurchaseOrderService
import com.pjb.immaapp.webservice.stockopname.StockOpnameService
import com.pjb.immaapp.webservice.usulan.UsulanPermintaanBarangService
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitApp {

    companion object {
        const val FIRST_PAGE = 1
        const val ITEM_PER_PAGE = 10
        const val API_KEY = 12345
        private const val BASE_URL = "http://09f02bafc978.ngrok.io/"
        const val UPLOAD_URL = "$BASE_URL/api/fpb/create"
        const val UPLOAD_MATERIAL_URL = BASE_URL+"api/fpb/create-material"


        private val interceptor: HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        private val client = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .addInterceptor(NetworkInterceptor())
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

        fun getLoginService(): LoginService {
            return retrofit.create(LoginService::class.java)
        }

        fun getPurchaseOrderService(): PurchaseOrderService {
            return retrofit.create(PurchaseOrderService::class.java)
        }

        fun getUpbService(): UsulanPermintaanBarangService {
            return retrofit.create(UsulanPermintaanBarangService::class.java)
        }

        fun getStockOpnameService(): StockOpnameService {
            return retrofit.create(StockOpnameService::class.java)
        }

    }

}