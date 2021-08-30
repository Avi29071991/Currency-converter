package com.avinash.paypay.test.foundation.networking.http

import com.avinash.paypay.test.foundation.networking.Network
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

sealed class EndpointResult {
    sealed class Decoded<T> {
        data class Value<T>(val value: T?) : Decoded<T>()
        data class Error<T>(val throwable: Throwable) : Decoded<T>()
    }

    data class Response(
        val body: String? = null
    ) : EndpointResult() {
        fun hasData() = !body.isNullOrBlank()

        inline fun <reified T> decodeResponse(): Decoded<T> {
            try {
                return if (!body.isNullOrBlank()) {
                    Decoded.Value(
                        Json {
                            ignoreUnknownKeys = true
                            isLenient = true
                        }.decodeFromString(string = body)
                    )
                } else {
                    return Decoded.Value(null)
                }
            } catch (ex: SerializationException) {
                return Decoded.Error(ex)
            }
        }
    }

    data class Error(
        val code: Int,
        val response: ErrorResponse,
        val requestInfo: Network.RequestInfo? = null,
        var headers: HttpHeaders? = null,
        var handled: Boolean = false
    ) : EndpointResult()
}
