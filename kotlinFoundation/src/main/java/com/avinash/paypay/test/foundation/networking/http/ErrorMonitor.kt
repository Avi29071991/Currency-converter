package com.avinash.paypay.test.foundation.networking.http

interface ErrorMonitor {
    fun onFailure(response: EndpointResult.Error): Boolean
}
