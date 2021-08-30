package com.avinash.paypay.test.foundation.networking.http

import kotlinx.serialization.Serializable

@Serializable
data class ErrorItem(
    val code: Int,
    val info: String? = null,
)