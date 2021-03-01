package com.pjb.immaapp.utils

import com.pjb.immaapp.utils.utilsentity.ErrorEntity

sealed class ImprovedNetworkState<T>{
    data class Success<T>(val data: T) : ImprovedNetworkState<T>()
    data class Error<T>(val error: ErrorEntity) : ImprovedNetworkState<T>()
}