package com.avinash.paypay.test.currencykotlin.endpoint

import com.avinash.paypay.test.foundation.networking.http.Endpoint
import com.avinash.paypay.test.foundation.networking.http.HttpBody

/**
 * [Endpoint] class used to provide appropriate URL data to network call for fetching live currency rate
 */
class LiveCurrencyRateEndpoint(private val url: String, private val token: String): Endpoint {
    override val baseURL: String
        get() = url

    override val path: String
        get() = "live"

    override val body: HttpBody
        get() {
            return mapOf(
                "access_key" to token
            )
        }
}