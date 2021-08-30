package com.avinash.paypay.test.foundation.networking.http

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val success: Boolean,
    val error: ErrorItem
)


