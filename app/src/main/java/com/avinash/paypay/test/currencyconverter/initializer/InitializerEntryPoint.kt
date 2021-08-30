package com.avinash.paypay.test.currencyconverter.initializer

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * [EntryPoint] for Initializer class which will help us to provide injection mechanism
 * to inject services and components in App start up library provided by android framework
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface InitializerEntryPoint {

    /**
     * Provide mechanism to inject services and components in [AppLaunchInitializer]
     */
    fun inject(initializer: AppLaunchInitializer)

    companion object {
        /**
         * Function to resolve [EntryPoint] for Initializer
         */
        fun resolve(context: Context): InitializerEntryPoint {
            return EntryPointAccessors.fromApplication(
                context,
                InitializerEntryPoint::class.java
            )
        }
    }
}