package com.pjb.immaapp.utils

class ImmaEventHandler<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    /**
     * mengembalikan content jika belum dihandle
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * mengembalikan event, meskipun sudah dihandle
     */
    fun peekContent(): T = content
}