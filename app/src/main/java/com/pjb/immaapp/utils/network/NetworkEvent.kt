package com.pjb.immaapp.utils.network

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

object NetworkEvent {
    private lateinit var subject: PublishSubject<NetworkState>
    private val compositeDisposableMap = HashMap<Any, CompositeDisposable>()

    private fun getSubject(): PublishSubject<NetworkState> {
        subject = PublishSubject.create()
        subject.subscribeOn(AndroidSchedulers.mainThread())
        return subject
    }

    private fun getCompositeSubscription(any: Any): CompositeDisposable {
        var compositeSubscription: CompositeDisposable? = compositeDisposableMap[any]
        if (compositeSubscription == null) {
            compositeSubscription = CompositeDisposable()
            compositeDisposableMap[any] = compositeSubscription
        }
        return compositeSubscription
    }

    fun publish(networkState: NetworkState) {
        Handler(Looper.getMainLooper())
            .post{getSubject().onNext(networkState)}
    }

    fun register(lifecycle: Any, action: Consumer<NetworkState>) {
        val disposable = getSubject().subscribe(action)
        getCompositeSubscription(lifecycle).add(disposable)
    }

    fun unRegister(lifecycle: Any) {
        val compositeSubscription  = compositeDisposableMap.remove(lifecycle)
        compositeSubscription?.dispose()
    }
}