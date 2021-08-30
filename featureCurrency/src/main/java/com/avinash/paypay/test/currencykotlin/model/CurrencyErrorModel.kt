package com.avinash.paypay.test.currencykotlin.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyErrorModel(
    val code: Int,
    val info: String? = null
)
