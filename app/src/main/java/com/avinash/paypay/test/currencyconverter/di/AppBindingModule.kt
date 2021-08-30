package com.avinash.paypay.test.currencyconverter.di

import com.avinash.paypay.test.currencyconverter.environment.AppInfoProviderImplementation
import com.avinash.paypay.test.currencyconverter.logging.ConsoleLogProvider
import com.avinash.paypay.test.foundation.environment.interfaces.AppInfoProvider
import com.avinash.paypay.test.foundation.logging.interfaces.LogProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

/**
 * Application binding module used for provided interfaces using Hilt
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingModule {

    /**
     * Binds implementation for [AppInfoProvider]
     */
    @Binds
    abstract fun bindsAppInfoProvider(provider: AppInfoProviderImplementation): AppInfoProvider

    /**
     * Binds implementation for [LogProvider]
     */
    @Binds
    @IntoSet
    abstract fun bindsLogProvider(provider: ConsoleLogProvider): LogProvider
}