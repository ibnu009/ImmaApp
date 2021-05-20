package com.pjb.immaapp.service.webservice.notification

import com.pjb.immaapp.data.entity.notification.NotificationModel
import com.pjb.immaapp.data.remote.response.ResponseDataTokenFcm
import com.pjb.immaapp.data.remote.response.ResponseRegistrationToken
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface NotificationService {

    @Multipart
    @POST("api/fpb/save/registration-code")
    fun saveRegistrationToken(
        @Part("api_key")apiKey: RequestBody,
        @Part("token")token: RequestBody,
        @Part("nama")name: RequestBody,
        @Part("id_user")idUser: RequestBody,
        @Part("token_fcm")tokenFcm: RequestBody
    ): Single<ResponseRegistrationToken>

    @POST("api/fpb/data/registration-code")
    @FormUrlEncoded
    fun getAuthoritiesToken(
        @Field("api_key")apiKey: String,
        @Field("token")token: String
    ): Observable<ResponseDataTokenFcm>
}