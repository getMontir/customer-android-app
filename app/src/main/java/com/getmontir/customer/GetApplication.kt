package com.getmontir.customer

import android.app.Application
import com.getmontir.customer.BuildConfig
import com.getmontir.customer.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import timber.log.Timber

open class GetApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // TIMBER
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // KOIN
        configureDi()
        loadKoinModules(appModules)
    }

    // CONFIGURATION ---
    open fun configureDi() = startKoin {
        androidContext(this@GetApplication)
        appModules
    }

    // PUBLIC API
    open fun proviceComponent() = appModules
}