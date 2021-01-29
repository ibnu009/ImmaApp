package com.pjb.immaapp.webservice.login

import com.pjb.immaapp.data.entity.User
import com.pjb.immaapp.data.entity.request.Credential
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {

    @POST("/api/auth/login")
    @FormUrlEncoded
    fun loginRequest(
        @Body request: Credential
    ): Observable<User>
}