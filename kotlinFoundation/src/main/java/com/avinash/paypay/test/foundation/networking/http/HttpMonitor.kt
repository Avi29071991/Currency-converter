package com.avinash.paypay.test.foundation.networking.http

import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.Throws

interface HttpMonitor {
    val interceptorType get() = HttpMonitorType.NETWORK

    @Throws(IOException::class)
    fun interceptRequest(request: Request): Request {
        return request
    }

    @Throws(IOException::class)
    fun interceptResponse(response: Response): Response {
        return response
    }
}
