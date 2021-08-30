package com.avinash.paypay.test.currencyconverter.initializer

import android.content.Context
import androidx.startup.Initializer
import com.avinash.paypay.test.currencykotlin.datasource.CurrencyDataSource
import com.avinash.paypay.test.foundation.environment.AppInfo
import com.avinash.paypay.test.foundation.environment.interfaces.AppInfoProvider
import com.avinash.paypay.test.foundation.logging.Log
import com.avinash.paypay.test.foundation.logging.interfaces.LogProvider
import java.util.Collections.emptyList
import javax.inject.Inject

/**
 * Initializer class used to initialize components and keep it handy when the app is launch
 *
 */
class AppLaunchInitializer: Initializer<Unit> {

    // Injecting LogProvider implementation
    @Inject
    lateinit var logProviders: Set<@JvmSuppressWildcards LogProvider>

    // Injecting AppInfoProvider implementation
    @Inject
    lateinit var appInfoProvider: AppInfoProvider

    override fun create(context: Context) {
        // Provision to inject components to Initializer class through entry point
        InitializerEntryPoint.resolve(context = context).inject(this)

        logProviders.forEach { Log.register(it) }
        AppInfo.register(appInfoProvider)

        // Setting base currency url for the currency app
        CurrencyDataSource.setBaseUrl(value = CURRENCY_BASE_URL)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return emptyList()
    }

    companion object {
        private const val CURRENCY_BASE_URL = "http://api.currencylayer.com/"
    }
}