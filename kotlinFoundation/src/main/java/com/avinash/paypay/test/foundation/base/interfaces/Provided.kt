package com.avinash.paypay.test.foundation.base.interfaces

/**
 * Core level generic interface used for performing operations on provider
 * This needs to be implemented by every capability of the application which provides data using a provider.
 *
 * @param T indicates the type of provider which Provided must adhere to perform register/unregister operations
 */
interface Provided<in T> {
    /**
     * Registers a provider for selected capability
     * @param provider Provider which needs to be registered
     * @return Boolean value indicating if the provider has been registered
     */
    fun register(provider: T): Boolean

    /**
     * Unregisters a provider for selected capability
     * @param provider Provider which needs to be unregistered
     * @return Boolean value indicating if the provider has been unregistered
     */
    fun unregister(provider: T): Boolean
}
