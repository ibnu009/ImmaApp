package com.pjb.immaapp.utils.utilsentity

import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection


class GeneralErrorHandler : ErrorHandler {

    override fun getError(throwable: Throwable): ErrorEntity {
        return when(throwable) {
            is IOException -> ErrorEntity.Network
            is HttpException -> {
                when(throwable.code()) {

                    // not found
                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound

                    // access denied
                    HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.AccessDenied

                    // wrong credential
                    HttpURLConnection.HTTP_UNAUTHORIZED -> ErrorEntity.UnauthorizedUser

                    // unavailable service
                    HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntity.ServiceUnavailable

                    // unknown error
                    else -> ErrorEntity.Unknown
                }
            }
            else -> ErrorEntity.Unknown
        }
    }
}