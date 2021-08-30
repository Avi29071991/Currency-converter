package com.avinash.paypay.test.foundation.environment.interfaces

import com.avinash.paypay.test.foundation.environment.Version

interface AppInfoProvider {
    /**
     * Name of the application
     */
    val name: String

    /**
     * Version name of the application `versionName`
     */
    val version: Version

    /**
     * Version code of the application `versionCode`
     */
    val versionCode: Long

    /**
     * Package name of the application
     */
    val packageName: String

    /**
     * Flag to find out whether its release build or not
     */
    val isReleaseBuild: Boolean

    /**
     * The authority of a FileProvider
     */
    val fileProviderAuthority: String
}