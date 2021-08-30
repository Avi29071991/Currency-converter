package com.avinash.paypay.test.currencyconverter.logging

import android.util.Log
import com.avinash.paypay.test.foundation.logging.interfaces.LogLevel
import com.avinash.paypay.test.foundation.logging.interfaces.LogProvider
import javax.inject.Inject

/**
 * Implementation class for [LogProvider].
 * This will be used to actually log data in console
 */
class ConsoleLogProvider @Inject constructor(): LogProvider {
    override fun log(level: LogLevel, tag: String, message: String, error: Throwable?) {
        when (level) {
            LogLevel.DEBUG -> Log.d(tag, message, error)
            LogLevel.INFO -> Log.i(tag, message, error)
            LogLevel.WARNING -> Log.w(tag, message, error)
            LogLevel.ERROR -> Log.e(tag, message, error)
            else -> Unit
        }
    }
}