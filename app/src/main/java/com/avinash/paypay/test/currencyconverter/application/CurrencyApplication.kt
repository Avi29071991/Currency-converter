package com.avinash.paypay.test.currencyconverter.application

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.AppInitializer
import androidx.work.Configuration
import com.avinash.paypay.test.currencyconverter.initializer.AppLaunchInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application class annotated with Hilt dependencies to let us know
 * that the application can inject dependencies generated using Hilt
 */
@HiltAndroidApp
class CurrencyApplication : Application(), Configuration.Provider {

    // Worker factory instance which helps in injecting Worker classes
    // This will also be used for configuring work manager on demand
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        init()
    }

    /**
     * Function to initialise injected components using App Startup jetpack library
     */
    private fun init() {
        AppInitializer.getInstance(this).initializeComponent(AppLaunchInitializer::class.java)
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}