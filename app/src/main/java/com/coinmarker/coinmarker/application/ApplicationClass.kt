package com.coinmarker.coinmarker.application

import android.app.Application
import com.coinmarker.coinmarker.BuildConfig
import timber.log.Timber

class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
