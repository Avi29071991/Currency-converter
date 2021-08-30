package com.avinash.paypay.test.foundation.networking.http

import java.io.InputStream

interface Endpoint {
    val baseURL: String
    val path: String
    val method: HttpMethod get() = HttpMethod.GET
    val monitorList: List<HttpMonitor>? get() = null
    val headers: HttpHeaders? get() = null
    val body: HttpBody? get() = null
    val encoding: HttpEncoding get() = HttpEncoding.NONE
    val requestTimeout: Long get() = 30L
    val cachePolicy: CachePolicy get() = CachePolicy.DEFAULT
    val followRedirects: Boolean get() = false
    val followSslRedirects: Boolean get() = false
    val httpStream: HttpStream? get() = null
    val isBlocking: Boolean
        get() {
            return method == HttpMethod.DELETE ||
                method == HttpMethod.PATCH ||
                method == HttpMethod.POST ||
                method == HttpMethod.PUT
        }
}

typealias HttpHeaders = Map<String, HttpHeader>
typealias HttpBody = Map<String, String>
typealias HttpStream = (HttpHeaders?, InputStream) -> Unit
