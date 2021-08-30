package com.avinash.paypay.test.foundation.networking.http

enum class HttpEncoding(val mediaType: String?) {
    JSON(mediaType = "application/json; charset=utf-8"),
    JSON_BYTE_ARRAY(mediaType = "application/json"),
    NONE(mediaType = null)
}