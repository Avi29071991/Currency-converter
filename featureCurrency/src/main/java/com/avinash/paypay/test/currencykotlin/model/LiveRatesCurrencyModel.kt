package com.avinash.paypay.test.currencykotlin.model

import com.avinash.paypay.test.foundation.serializers.LocalDateTimeAsLongSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import java.time.LocalDateTime

@Serializable
data class LiveRatesCurrencyModel(
    val success: Boolean? = null,
    val term: String? = null,
    val privacy: String? = null,
    @Serializable(with = LocalDateTimeAsLongSerializer::class)
    val timestamp: LocalDateTime? = null,
    val source: String? = null,
    val quotes: JsonObject? = null,
    val error: CurrencyErrorModel? = null
) {
    // Mapping currency rates json object to our list
    val currencyQuotes: List<CurrencyRate>
    get() {
        val quotesList = mutableListOf<CurrencyRate>()
        quotes?.entries?.forEach {
            if (!source.isNullOrBlank()) {
                quotesList.add(
                    CurrencyRate(
                        currencyCode = it.key.removeRange(0..2),
                        currencyRate = it.value.toString().toBigDecimal()
                    )
                )
            }
        }

        return quotesList
    }
}
