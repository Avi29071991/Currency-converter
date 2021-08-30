package com.avinash.paypay.test.foundation.networking

import com.avinash.paypay.test.foundation.logging.Log
import com.avinash.paypay.test.foundation.networking.http.CachePolicy
import com.avinash.paypay.test.foundation.networking.http.Endpoint
import com.avinash.paypay.test.foundation.networking.http.EndpointResult
import com.avinash.paypay.test.foundation.networking.http.ErrorParseException
import com.avinash.paypay.test.foundation.networking.http.ErrorResponse
import com.avinash.paypay.test.foundation.networking.http.HttpBody
import com.avinash.paypay.test.foundation.networking.http.HttpEncoding
import com.avinash.paypay.test.foundation.networking.http.HttpHeader
import com.avinash.paypay.test.foundation.networking.http.HttpHeaders
import com.avinash.paypay.test.foundation.networking.http.HttpInterceptor
import com.avinash.paypay.test.foundation.networking.http.HttpMethod
import com.avinash.paypay.test.foundation.networking.http.HttpMonitor
import com.avinash.paypay.test.foundation.networking.http.HttpMonitorType
import com.avinash.paypay.test.foundation.networking.http.ResponseHeader
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLHandshakeException

/**
 * Http client used to send HTTP requests and read their responses.
 *
 * ## Making a network call
 *
 * This example shows how to make a simple network call and receive the response as onFailure or onSuccessful.
 *
 * '''
 * Network.call(endpoint, object : endpointResult { ... })
 * '''
 *
 * ## Adding call monitor
 *
 * This example shows how to add monitors which are used to intercept calls before and after execution.
 *
 * '''
 * Network.addHttpMonitor(httpMonitor) or Network.addHttpMonitor(httpMonitorList)
 * '''
 */
object Network {

    private var okHttpClient = OkHttpClient()
    private var httpHeaderMap = mutableMapOf<String, HttpHeader>()

    // Sent through the tag to the network interceptors
    // Both gives information to inform the interceptor and can be updated to
    // indicate that the request was actually sent
    data class RequestInfo(val endpoint: Endpoint, var requestWasSent: Boolean = false)

    fun call(endpoint: Endpoint, callback: Result<EndpointResult>.() -> Unit) {
        try {
            val okHttpClient = buildOkHttpClient(endpoint = endpoint)
            val tag = RequestInfo(endpoint = endpoint)
            val request = buildRequest(endpoint = endpoint, tag = tag)

            okHttpClient.newCall(request = request).enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        processFailure(
                            exception = e,
                            requestInfo = tag,
                            callback = callback
                        )
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            processResponse(response = response, endpoint = endpoint, callback = callback)
                        }
                    }
                }
            )
        } catch (ex: Exception) {
            Log.error(message = "Failed to execute network call, for API = ${endpoint.path}", error = ex)

            callback.invoke(Result.failure(exception = ex))
        }
    }

    private fun addInterceptor(httpMonitor: HttpMonitor, okHttpClientBuilder: OkHttpClient.Builder) {
        val httpInterceptor = HttpInterceptor(httpMonitor = httpMonitor)

        when (httpMonitor.interceptorType) {
            HttpMonitorType.APPLICATION -> okHttpClientBuilder.addInterceptor(interceptor = httpInterceptor)
            HttpMonitorType.NETWORK -> okHttpClientBuilder.addNetworkInterceptor(interceptor = httpInterceptor)
        }
    }

    private fun buildOkHttpClient(endpoint: Endpoint): OkHttpClient {
        val okHttpClientBuilder = okHttpClient.newBuilder()
        okHttpClientBuilder.callTimeout(timeout = endpoint.requestTimeout, unit = TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(timeout = endpoint.requestTimeout, unit = TimeUnit.SECONDS)
        okHttpClientBuilder.followRedirects(followRedirects = endpoint.followRedirects)
        okHttpClientBuilder.followSslRedirects(followProtocolRedirects = endpoint.followSslRedirects)

        addRequestHttpMonitor(endpoint = endpoint, okHttpClientBuilder = okHttpClientBuilder)

        return okHttpClientBuilder.build()
    }

    private fun addRequestHttpMonitor(endpoint: Endpoint, okHttpClientBuilder: OkHttpClient.Builder) {
        endpoint.monitorList?.run {
            this.forEach {
                addInterceptor(httpMonitor = it, okHttpClientBuilder = okHttpClientBuilder)
            }
        }
    }

    private fun buildRequest(endpoint: Endpoint, tag: Any): Request {
        val requestBuilder = Request.Builder()
        val httpUrlBuilder = buildUrl(endpoint = endpoint)

        buildHeaders(endpoint = endpoint, requestBuilder = requestBuilder)
        buildMethod(endpoint = endpoint, requestBuilder = requestBuilder, httpUrlBuilder = httpUrlBuilder)
        buildCache(endpoint = endpoint, requestBuilder = requestBuilder)

        // let's make the endpoint available in interceptors
        requestBuilder.tag(tag)

        requestBuilder.url(url = httpUrlBuilder.build())

        return requestBuilder.build()
    }

    private fun buildUrl(endpoint: Endpoint): HttpUrl.Builder {
        val endpointBaseUrl = URL(endpoint.baseURL)

        val builder = HttpUrl.Builder()
            .scheme(scheme = endpointBaseUrl.protocol)
            .host(host = endpointBaseUrl.host)
            .addPathSegments(pathSegments = validatePath(path = endpointBaseUrl.path))

        if (endpoint.path.isNotEmpty()) {
            Log.debug("Endpoint's path = ${endpoint.path}")
            builder.addPathSegments(pathSegments = validatePath(path = endpoint.path))
        }

        // Added support for providing custom port number to the URL
        if (endpointBaseUrl.port != -1) {
            builder.port(port = endpointBaseUrl.port)
        }

        return builder
    }

    private fun buildHeaders(endpoint: Endpoint, requestBuilder: Request.Builder) {
        val headersBuilder = Headers.Builder()

        addNetworkHeaders(
            httpMethod = endpoint.method,
            headersBuilder = headersBuilder
        )

        addRequestHeaders(requestHeadersMap = endpoint.headers, headersBuilder = headersBuilder)

        val headers = headersBuilder.build()

        if (headers.size > 0) {
            requestBuilder.headers(headers = headers)
        }
    }

    private fun buildMethod(endpoint: Endpoint, requestBuilder: Request.Builder, httpUrlBuilder: HttpUrl.Builder) {
        var requestBody: RequestBody? = null

        when (endpoint.method) {
            HttpMethod.GET,
            HttpMethod.HEAD -> addQueryParameter(endpoint = endpoint, httpUrlBuilder = httpUrlBuilder)
            HttpMethod.DELETE,
            HttpMethod.PATCH,
            HttpMethod.POST,
            HttpMethod.PUT -> requestBody = buildBody(endpoint = endpoint)
        }

        requestBuilder.method(method = endpoint.method.value, body = requestBody)
    }

    private fun addQueryParameter(endpoint: Endpoint, httpUrlBuilder: HttpUrl.Builder) {
        val parameters = endpoint.body

        parameters?.forEach {
            httpUrlBuilder.addQueryParameter(name = it.key, value = it.value)
        }
    }

    private fun buildBody(endpoint: Endpoint): RequestBody {
        val httpBody = endpoint.body

        return if (httpBody != null) {
            when (endpoint.encoding) {
                HttpEncoding.JSON,
                HttpEncoding.JSON_BYTE_ARRAY -> buildRequestBody(
                    httpBody = httpBody,
                    encoding = endpoint.encoding
                )
                else -> buildBodyForm(httpBody = httpBody)
            }
        } else {
            // RequestBody required for DELETE, PATCH, POST and PUT http method.
            "".toRequestBody()
        }
    }

    private fun buildRequestBody(httpBody: HttpBody, encoding: HttpEncoding): RequestBody {
        val body = httpBody.values.joinToString(separator = "")
        val mediaType = encoding.mediaType?.toMediaTypeOrNull()

        val charset = if (mediaType == null) {
            Charsets.UTF_8
        } else {
            mediaType.charset() ?: Charsets.UTF_8
        }

        return body.toByteArray(charset = charset).toRequestBody(contentType = mediaType)
    }

    private fun buildBodyForm(httpBody: HttpBody): FormBody {
        val formBody = FormBody.Builder()

        httpBody.forEach {
            formBody.add(name = it.key, value = it.value)
        }

        return formBody.build()
    }

    private fun buildCache(endpoint: Endpoint, requestBuilder: Request.Builder) {
        val cachePolicy = endpoint.cachePolicy

        if (cachePolicy != CachePolicy.DEFAULT) {
            val cacheControlBuilder = CacheControl.Builder()

            when (cachePolicy) {
                CachePolicy.NO_CACHE -> cacheControlBuilder.noCache()
                CachePolicy.NO_STORE -> cacheControlBuilder.noStore()
                CachePolicy.ONLY_IF_CACHED -> cacheControlBuilder.onlyIfCached()
                CachePolicy.SERVER -> cacheControlBuilder.maxAge(maxAge = 0, timeUnit = TimeUnit.SECONDS)
                else -> Unit
            }

            requestBuilder.cacheControl(cacheControl = cacheControlBuilder.build())
        }
    }

    private fun addNetworkHeaders(httpMethod: HttpMethod, headersBuilder: Headers.Builder) {
        if (httpHeaderMap.isNotEmpty()) {
            httpHeaderMap.forEach {
                val httpHeader = it.value
                val httpHeaderMethod = httpHeader.method

                if ((httpHeaderMethod == null || httpMethod == httpHeaderMethod)) {
                    headersBuilder.add(name = it.key, value = httpHeader.value)
                }
            }
        }
    }

    private fun addRequestHeaders(
        requestHeadersMap: Map<String, HttpHeader>?,
        headersBuilder: Headers.Builder
    ) {

        requestHeadersMap?.forEach {
            val httpHeader = it.value

            headersBuilder.add(name = it.key, value = httpHeader.value)
        }
    }

    private fun processFailure(exception: IOException, requestInfo: RequestInfo, callback: Result<EndpointResult>.() -> Unit) {
        Log.debug("Exception ${exception.javaClass.canonicalName ?: "<anonymous>"} thrown from network call.")
        when (exception) {
            is SocketTimeoutException, is InterruptedIOException -> {
                processErrorResult(
                    errorResponse = NetworkError.TIMEOUT.value(),
                    requestInfo = requestInfo,
                    callback = callback
                )
            }
            is SSLHandshakeException -> {
                processErrorResult(
                    errorResponse = NetworkError.SSL_FAILURE.value(),
                    requestInfo = requestInfo,
                    callback = callback
                )
            }
            else -> { // This is an IO exception so we aren't able to talk to the other end.
                // e.g. UnknownHostException, ConnectException, NoRouteToHostException, SocketException
                processErrorResult(
                    errorResponse = NetworkError.NO_INTERNET_CONNECTION.value(),
                    requestInfo = requestInfo,
                    callback = callback
                )
            }
        }
    }

    private fun processResponse(response: Response, endpoint: Endpoint, callback: Result<EndpointResult>.() -> Unit) {
        when {
            response.isSuccessful -> processSuccessful(endpoint = endpoint, response = response, callback = callback)
            else -> processError(response = response, endpoint = endpoint, callback = callback)
        }
    }

    private fun processError(response: Response, endpoint: Endpoint, callback: Result<EndpointResult>.() -> Unit) {
        val responseData = responseBody(endpoint = endpoint, response = response)

        try {
            val errorResponse: ErrorResponse = if (responseData.isNullOrBlank()) {
                NetworkError.NO_RESPONSE.value()
            } else {
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }.decodeFromString(string = responseData)
            }

            processErrorResult(
                response = response,
                errorResponse = errorResponse,
                requestInfo = null,
                callback = callback
            )
        } catch (ex: Exception) {
            val errorParseException = ErrorParseException(message = "Failed to parse error response.", cause = ex)

            callback(Result.failure(exception = errorParseException))
        }
    }

    private fun processErrorResult(
        response: Response? = null,
        errorResponse: ErrorResponse,
        requestInfo: RequestInfo?,
        callback: Result<EndpointResult>.() -> Unit
    ) {
        val responseCode = response?.code ?: 0
        val errorResult = EndpointResult.Error(code = responseCode, response = errorResponse, requestInfo = requestInfo)

        callback(Result.success(value = errorResult))
    }

    private fun processSuccessful(endpoint: Endpoint, response: Response, callback: Result<EndpointResult>.() -> Unit) {
        try {
            val data = when (response.code) {
                HTTP_OK,
                HTTP_CREATED,
                HTTP_ACCEPTED,
                HTTP_NO_CONTENT -> responseBody(endpoint = endpoint, response = response)

                else -> null
            }
            callback(Result.success(value = EndpointResult.Response(body = data)))
        } catch (exception: IOException) {
            Log.error("processSuccessful: ", exception)
            callback(Result.failure(exception))
        }
    }

    private fun responseBody(endpoint: Endpoint, response: Response): String? {
        val responseBody = response.body
        return if (endpoint.httpStream != null) {
            responseBody?.let { body ->
                try {
                    val headers = parseResponseHeaders(response)
                    endpoint.httpStream?.invoke(headers, body.byteStream())
                    null
                } catch (exception: IOException) {
                    Log.error("responseBody: ", exception)
                    throw exception
                } finally {
                    body.byteStream().close()
                }
            }
        } else {
            val responseData = responseBody?.string()
            if (responseData.isNullOrBlank()) {
                null
            } else {
                responseData
            }
        }
    }

    private fun parseResponseHeaders(response: Response?): HttpHeaders? {
        if (response != null && response.headers.size > 0) {
            val httpHeaders = mutableMapOf<String, HttpHeader>()

            for (header in response.headers) {
                httpHeaders[header.first] = ResponseHeader(value = header.second)
            }

            return httpHeaders
        }

        return null
    }

    private fun validatePath(path: String): String {
        return if (path.startsWith("/")) {
            path.substring(1)
        } else {
            path
        }
    }

    /**
     * HTTP Status-Code 200: OK.
     */
    private const val HTTP_OK = 200

    /**
     * HTTP Status-Code 201: Created.
     */
    private const val HTTP_CREATED = 201

    /**
     * HTTP Status-Code 202: Accepted.
     */
    private const val HTTP_ACCEPTED = 202

    /**
     * HTTP Status-Code 204: No Content.
     */
    private const val HTTP_NO_CONTENT = 204
}
