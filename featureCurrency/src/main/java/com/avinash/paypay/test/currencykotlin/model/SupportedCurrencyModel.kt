package com.avinash.paypay.test.currencykotlin.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class SupportedCurrencyModel(
    val success: Boolean? = null,
    val term: String? = null,
    val privacy: String? = null,
    val currencies: JsonObject? = null,
    val error: CurrencyErrorModel? = null
) {
    // Mapping supported currencies json object to our list
    val currencyList: List<CurrencyData>
    get() {
        val currenciesData = mutableListOf<CurrencyData>()
        currencies?.entries?.forEach {
            currenciesData.add(
                CurrencyData(currencyCode = it.key, currencyCountryName = it.value.toString())
            )
        }

        return currenciesData
    }
}
