package com.pjb.immaapp.utils

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    UNAUTHORISED
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
    }
}