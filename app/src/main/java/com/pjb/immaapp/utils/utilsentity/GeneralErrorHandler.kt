package com.pjb.immaapp.utils.utilsentity

import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.Status
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection


class GeneralErrorHandler : ErrorHandler {

    override fun getError(throwable: Throwable): NetworkState {
        return when(throwable) {
//           no Connection Error
            is IOException -> NetworkState.ERROR
            
//            HTTP Error
            is HttpException -> {
                when(throwable.code()) {
                    // not found
                    HttpURLConnection.HTTP_NOT_FOUND -> NetworkState(Status.NOT_FOUND)

                    // access denied
                    HttpURLConnection.HTTP_FORBIDDEN -> NetworkState(Status.UNAUTHORISED)

                    // HTTP Conflict
                    HttpURLConnection.HTTP_CONFLICT -> NetworkState(Status.CONFLICT)

                    // wrong credential
                    HttpURLConnection.HTTP_UNAUTHORIZED -> NetworkState(Status.UNAUTHORISED)

                    // unavailable service
                    HttpURLConnection.HTTP_UNAVAILABLE -> NetworkState(Status.FAILED)

                    // Expired Token
                    HttpURLConnection.HTTP_CLIENT_TIMEOUT -> NetworkState(Status.EXPIRETOKEN)

                    // unknown error
                    else -> NetworkState(Status.UNKNOWN)
                }
            }
            else -> NetworkState(Status.UNKNOWN)
        }
    }
}