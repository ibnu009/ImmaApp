package com.pjb.immaapp.utils.utilsentity

import com.pjb.immaapp.utils.NetworkState

interface ErrorHandler {
    fun getError(throwable: Throwable): NetworkState
}