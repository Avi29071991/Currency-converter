package com.avinash.paypay.test.currencykotlin.endpoint

import com.avinash.paypay.test.foundation.networking.http.Endpoint
import com.avinash.paypay.test.foundation.networking.http.HttpBody

/**
 * [Endpoint] class used to provide appropriate URL data to network call for fetching supported currency list
 */
class SupportedCurrencies(private val url: String, private val token: String): Endpoint {
    override val baseURL: String
        get() = url

    override val path: String
        get() = "list"

    override val body: HttpBody
        get() {
            return mapOf(
                "access_key" to token
            )
        }
}
