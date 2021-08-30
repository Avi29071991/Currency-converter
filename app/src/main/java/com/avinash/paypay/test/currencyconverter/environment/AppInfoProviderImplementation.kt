package com.avinash.paypay.test.currencyconverter.environment

import android.content.Context
import com.avinash.paypay.test.currencyconverter.BuildConfig
import com.avinash.paypay.test.currencyconverter.R
import com.avinash.paypay.test.foundation.environment.Version
import com.avinash.paypay.test.foundation.environment.interfaces.AppInfoProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Implementation of [AppInfoProvider] used to provide information for the application
 * like app name, version name, version code, package name, etc.
 */
class AppInfoProviderImplementation @Inject constructor(@ApplicationContext private val context: Context) :
    AppInfoProvider {
    override val name: String
        get() = context.resources.getString(R.string.app_name)

    override val version: Version
        get() = Version(
            version = context.packageManager.getPackageInfo(
                context.packageName,
                0
            ).versionName
        )

    override val versionCode: Long
        get() = context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode

    override val packageName: String
        get() = context.packageName

    override val isReleaseBuild: Boolean
        get() = !BuildConfig.DEBUG

    override val fileProviderAuthority: String
        get() = "$packageName.provider"
}