package com.avinash.paypay.test.foundation.networking.http

enum class CachePolicy {
    /** Use default cache provided by the client. */
    DEFAULT,
    /** Don't accept an unvalidated cached response. */
    NO_CACHE,
    /** Don't store the server's response in any cache. */
    NO_STORE,
    /**
     * Only accept the response if it is in the cache. If the response isn't cached, a `504
     * Unsatisfiable Request` response will be returned.
     */
    ONLY_IF_CACHED,
    /** Force a cached response to be validated by the server. */
    SERVER
}