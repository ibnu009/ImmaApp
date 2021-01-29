package com.pjb.immaapp

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}