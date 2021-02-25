package com.pjb.immaapp.utils.utilsentity

interface ErrorHandler {
    fun getError(throwable: Throwable): ErrorEntity
}