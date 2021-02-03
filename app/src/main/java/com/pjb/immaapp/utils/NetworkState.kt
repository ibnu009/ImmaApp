package com.pjb.immaapp.utils

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status) {
    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS)
        val LOADING: NetworkState = NetworkState(Status.RUNNING)
        val ERROR: NetworkState = NetworkState(Status.FAILED)
        val ENDOFLIST: NetworkState = NetworkState(Status.FAILED)
    }
}