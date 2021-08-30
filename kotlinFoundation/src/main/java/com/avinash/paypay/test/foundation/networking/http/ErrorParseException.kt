package com.avinash.paypay.test.foundation.networking.http

import java.lang.Exception

/**
 * Custom exception class used to capture exception data related to Network operations
 */
class ErrorParseException : Exception {
    /**
     * @constructor Secondary constructor containing message and exception information
     * @param message Message provided when the exception occurs
     * @param cause Cause of the exception
     */
    constructor(message: String, cause: Throwable) : super(message, cause)

    /**
     * @constructor Secondary constructor containing message information
     * @param message Message provided when the exception occurs
     */
    constructor(message: String) : super(message)
}
