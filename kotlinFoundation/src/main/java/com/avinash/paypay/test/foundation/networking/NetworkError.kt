package com.avinash.paypay.test.foundation.networking

import com.avinash.paypay.test.foundation.networking.http.ErrorResponse
import com.avinash.paypay.test.foundation.networking.http.ErrorItem

enum class NetworkError(val code: Int, val message: String) {
    NO_RESPONSE(code = -1, message = "An unexpected response has been received by the server."),
    NO_INTERNET_CONNECTION(code = -2, message = "No internet connection."),
    SSL_FAILURE(code = -3, message = "SSL failure, unable to establish a secure connection."),
    TIMEOUT(code = -4, message = "Failed to receive response from the server within a given time.");

    fun value(): ErrorResponse {
        val errorItem = ErrorItem(
            code = code,
            info = message
        )

        return ErrorResponse(success = false, error = errorItem)
    }
}