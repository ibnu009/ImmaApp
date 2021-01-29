package com.pjb.immaapp.webservice.login

import com.pjb.immaapp.data.entity.User
import com.pjb.immaapp.data.entity.request.Credential
import com.pjb.immaapp.data.remote.response.ResponseUser
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {

    @POST("/api/auth/login")
    fun loginRequest(
        @Body request: Credential
    ): Observable<ResponseUser>
}