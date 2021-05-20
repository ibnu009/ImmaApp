package com.pjb.immaapp.utils

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    UNAUTHORISED,
    EXPIRETOKEN,
    FORBIDDEN,
    BAD_GATEAWAY,
    CONFLICT,
    SERVER_NOT_FOUND,
    UNKNOWN
}

class NetworkState(val status: Status) {
    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS)
        val LOADING: NetworkState = NetworkState(Status.RUNNING)
        val ERROR: NetworkState = NetworkState(Status.FAILED)
        val ENDOFLIST: NetworkState = NetworkState(Status.FAILED)
        val USERNOTFOUND: NetworkState = NetworkState(Status.UNAUTHORISED)
        val FAILEDTOADD: NetworkState = NetworkState(Status.FAILED)
        val EMPTYDATA: NetworkState = NetworkState(Status.FAILED)
        val CONFLICT: NetworkState = NetworkState(Status.CONFLICT)
        val EXPIRETOKEN: NetworkState = NetworkState(Status.EXPIRETOKEN)
        val FORBIDDEN: NetworkState = NetworkState(Status.FORBIDDEN)
        val UNKNOWN: NetworkState = NetworkState(Status.UNKNOWN)
        val BAD_GATEAWAY: NetworkState = NetworkState(Status.BAD_GATEAWAY)
        val SERVER_NOT_FOUND: NetworkState = NetworkState(Status.SERVER_NOT_FOUND)

    }
}