package com.avinash.paypay.test.foundation.networking.http

import java.util.Base64

/**
 * A representation of a single HTTP header's value / scope pair.
 *
 * @property access of the header.
 * @property method of the header.
 * @property value of the header.
 */
interface HttpHeader {
    val method: HttpMethod?
    val value: String
}

data class RequestHeader(
    override val method: HttpMethod? = null,
    override val value: String
) : HttpHeader

data class ResponseHeader(
    override val value: String
) : HttpHeader {
    override val method: HttpMethod? = null
}

class HttpHeaderName {
    companion object {
        val acceptCharset get() = "Accept-Charset"
        val acceptLanguage get() = "Accept-Language"
        val acceptEncoding get() = "Accept-Encoding"
        val authorization get() = "Authorization"
        val contentDisposition get() = "Content-Disposition"
        val userAgent get() = "User-Agent"
    }
}

class HttpHeaderValue {
    companion object {
        /**
         * Provides preferred encoding header value
         */
        val preferredEncoding: String
            get() {
                val list = listOf("br", "gzip", "deflate")

                var count = 0

                return list.joinToString {
                    quality(index = count++, value = it)
                }
            }

        /**
         * Returns a 'Basic Authorization' header value using the 'username' and 'password' provided.
         *
         * @param username to be added to the header.
         * @param password to be added to the header.
         *
         * @return 'Basic Authorization' http header value.
         */
        fun authorizationBasic(username: String, password: String): String {
            val credentialByteArray = "$username:$password".toByteArray(charset = Charsets.UTF_8)
            val credentialEncoded = Base64.getEncoder().encodeToString(credentialByteArray)

            return "Basic $credentialEncoded"
        }

        /**
         * Returns a 'Bearer Authorization' header value using the 'bearerToken' provided
         *
         * @param value to be added to the header.
         *
         * @return 'Bearer Authorization' http header value.
         */
        fun authorizationBearerToken(value: String): String {
            return "Bearer $value"
        }

        private fun quality(index: Int, value: String): String {
            val quality = 1.0 - (index * 0.1)

            return "$value;q=$quality"
        }
    }
}
