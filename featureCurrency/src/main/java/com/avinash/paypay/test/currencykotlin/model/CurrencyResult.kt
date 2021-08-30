package com.avinash.paypay.test.currencykotlin.model

import com.avinash.paypay.test.foundation.networking.http.EndpointResult

sealed class CurrencyResult<T> {

    data class Value<T>(
        val data: T?
    ) : CurrencyResult<T>()

    data class Error<T>(val error: EndpointResult.Error? = null, val throwable: Throwable? = null): CurrencyResult<T>()
}