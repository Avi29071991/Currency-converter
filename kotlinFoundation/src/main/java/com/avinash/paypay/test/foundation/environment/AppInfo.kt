package com.avinash.paypay.test.foundation.environment

import com.avinash.paypay.test.foundation.base.Provider
import com.avinash.paypay.test.foundation.base.interfaces.Capability
import com.avinash.paypay.test.foundation.environment.interfaces.AppInfoProvider

object AppInfo: Provider<AppInfoProvider>(), Capability {

    /**
     * Provides name of the application
     * @return Name of the application
     */
    fun name() = provider!!.name

    /**
     * Provides version name of the application
     * @return Version name of the application `versionName`
     */
    fun version() = provider!!.version

    /**
     * Provides version code of the application
     * @return Version code of the application `versionCode`
     */
    fun versionCode() = provider!!.versionCode

    /**
     * Provides package name of the application
     * @return Package name of the application
     */
    fun packageName() = provider!!.packageName

    /**
     * Provides flag whether its release build
     * @return flag whether its release build
     */
    fun isReleaseBuild() = provider!!.isReleaseBuild

    /**
     * Provides authority for file provider
     * @return the authority of file provider
     */
    fun fileProviderAuthority() = provider!!.fileProviderAuthority
}