package com.pjb.immaapp.utils

import com.bumptech.glide.load.engine.Resource
import com.pjb.immaapp.utils.utilsentity.ErrorEntity
import okhttp3.ResponseBody

sealed class ImprovedNetworkState<T>{
    data class Success<T>(val data: T) : ImprovedNetworkState<T>()
    data class Error<T>(val error: ErrorEntity) : ImprovedNetworkState<T>()
}