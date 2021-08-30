package com.avinash.paypay.test.currencykotlin.model

import java.math.BigDecimal

data class CurrencyData(val currencyCode: String, val currencyCountryName: String)

data class CurrencyRate(val currencyCode: String, val currencyRate: BigDecimal)
