package com.pjb.immaapp.utils.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkInterceptor(): Interceptor {
    private val networkEvent = NetworkEvent
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        try {
            when (response.code) {
                401 -> networkEvent.publish(NetworkState.UNAUTHORISED)
                503 -> networkEvent.publish(NetworkState.NO_RESPONSE)
            }
        } catch (e: IOException) {
            networkEvent.publish(NetworkState.NO_RESPONSE)
        }
        return response
    }

}