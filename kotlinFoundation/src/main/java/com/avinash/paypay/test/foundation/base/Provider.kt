package com.avinash.paypay.test.foundation.base

import com.avinash.paypay.test.foundation.base.interfaces.Provided


/**
 * Class which needs to be extended by every capability which needs a single provider to perform its task
 * @param P indicates the type to which the [Provider] must adhere to perform [Provided] operations
 */
open class Provider<P> : Provided<P> {

    /**
     * Provider of type <P> used to perform necessary operations for a capability
     */
    var provider: P? = null
        private set

    /**
     * Registers the given provider
     * @param provider Provider which needs to be registered for a capability
     * @return Boolean value indicating that the provider has been registered
     */
    override fun register(provider: P): Boolean {
        this.provider = provider
        return true
    }

    /**
     * Unregisters the given provider
     * @param provider Provider which needs to be unregistered
     * @return Boolean value indicating if the provider has been unregistered
     */
    override fun unregister(provider: P): Boolean {
        this.provider = null
        return true
    }
}
