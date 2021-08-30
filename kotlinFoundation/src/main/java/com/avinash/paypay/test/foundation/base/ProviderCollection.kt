package com.avinash.paypay.test.foundation.base

import com.avinash.paypay.test.foundation.base.interfaces.Provided
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Class which needs to be extended by every capability which needs a multiple providers to perform its task
 * @param P indicates the type to which the [ProviderCollection] must adhere to perform [Provided] operations
 */
open class ProviderCollection<P> : Provided<P> {
    /**
     * Container for list of providers which will be added or removed for a capability
     */
    var providers: ConcurrentLinkedQueue<P> = ConcurrentLinkedQueue()
        private set

    /**
     * Registers the given provider by adding it to the providers list
     * @param provider Provider which needs to be registered for a capability
     * @return Boolean value indicating that the provider has been registered
     */
    override fun register(provider: P): Boolean {
        return providers.add(provider)
    }

    /**
     * Unregisters the given provider by removing it to the providers list
     * @param provider Provider which needs to be unregistered
     * @return Boolean value indicating if the provider has been unregistered
     */
    override fun unregister(provider: P): Boolean {
        return providers.remove(provider)
    }
}
