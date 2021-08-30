package com.avinash.paypay.test.foundation.networking.http

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.Throws

internal class HttpInterceptor(private val httpMonitor: HttpMonitor) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // TODO: Add debug logging, that is why they are originalRequest and originalResponse variable.
        val originalRequest = chain.request()
        val request = httpMonitor.interceptRequest(request = originalRequest)

        val originalResponse = chain.proceed(request = request)
        return httpMonitor.interceptResponse(response = originalResponse)
    }
}
