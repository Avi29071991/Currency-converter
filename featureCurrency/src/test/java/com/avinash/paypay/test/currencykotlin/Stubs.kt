package com.avinash.paypay.test.currencykotlin

enum class Stubs(val rawValue: String) {
    LIVE_RATES("liveRates"),
    SUPPORTED_CURRENCIES("supportedCurrency");

    fun getResponse(): String {
        val fileInputStream = javaClass.classLoader?.getResourceAsStream("$rawValue.json")
        return fileInputStream?.bufferedReader()?.readText() ?: ""
    }
}