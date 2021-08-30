package com.avinash.paypay.test.foundation.base.interfaces

/**
 * Core level interface
 * This needs to be implemented by other interfaces like [Capability] etc.
 */
interface Concern {

    /**
     * Determines if the concern is enabled, default value set to `true`
     * @return Boolean value indicating that the concern is enabled
     */
    val enable: Boolean
        get() = true

    /**
     * Determines if the concern is eligible, default value set to `true`
     * @return Boolean value indicating that the concern is eligible
     */
    val eligible: Boolean
        get() = true

    /**
     * Determines if the concern is available.
     * Value if calculated using [enable] and [eligible]
     * @return Boolean value indicating that the concern is available
     */
    val available: Boolean
        get() = enable && eligible
}
