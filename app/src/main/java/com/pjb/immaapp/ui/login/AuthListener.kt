package com.pjb.immaapp.ui.login

import androidx.lifecycle.LiveData
import com.pjb.immaapp.data.entity.User
import com.pjb.immaapp.data.entity.request.Credential
import com.pjb.immaapp.data.remote.response.ResponseUser

interface AuthListener {
    fun onAuthenticating()
    fun onSuccess(credential: Credential,responseSuccess: LiveData<User>)
    fun onFailure(message: String)
}