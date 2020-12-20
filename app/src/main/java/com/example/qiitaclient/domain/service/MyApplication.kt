package com.example.qiitaclient.domain.service

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }

}