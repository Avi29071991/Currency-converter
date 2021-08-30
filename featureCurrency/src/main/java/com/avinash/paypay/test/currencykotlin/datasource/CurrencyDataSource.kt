package com.avinash.paypay.test.currencykotlin.datasource

import com.avinash.paypay.test.currencykotlin.endpoint.LiveCurrencyRateEndpoint
import com.avinash.paypay.test.currencykotlin.endpoint.SupportedCurrencies
import com.avinash.paypay.test.currencykotlin.model.CurrencyResult
import com.avinash.paypay.test.currencykotlin.model.LiveRatesCurrencyModel
import com.avinash.paypay.test.currencykotlin.model.SupportedCurrencyModel
import com.avinash.paypay.test.foundation.networking.Network
import com.avinash.paypay.test.foundation.networking.http.EndpointResult
import java.lang.IllegalStateException

/**
 * Data source class used to perform API calls
 */
object CurrencyDataSource {

    private const val ACCESS_TOKEN = "3087c27f7666ae9ecfa67d6225a5a4e3"
    private var baseUrl: String? = null
    private const val INVALID_RESPONSE_ERROR = "Invalid content response"

    fun setBaseUrl(value: String) {
        baseUrl = value
    }

    /**
     * Fetches the supported countries list from Currency API
     * @param callback provides callback with api response
     */
    fun fetchSupportedCurrencies(callback: (CurrencyResult<SupportedCurrencyModel>) -> Unit) {
        if (!baseUrl.isNullOrBlank()) {
            Network.call(
                SupportedCurrencies(url = baseUrl!!, token = ACCESS_TOKEN)
            ) {
                onSuccess {
                    when (it) {
                        is EndpointResult.Response -> {
                            when (val decodedData = it.decodeResponse<SupportedCurrencyModel>()) {
                                is EndpointResult.Decoded.Value -> {
                                    callback.invoke(
                                        CurrencyResult.Value(
                                            data = decodedData.value
                                        )
                                    )
                                }

                                is EndpointResult.Decoded.Error -> {
                                    callback.invoke(
                                        CurrencyResult.Error(
                                            throwable = IllegalStateException(INVALID_RESPONSE_ERROR)
                                        )
                                    )
                                }
                            }
                        }

                        is EndpointResult.Error -> {
                            callback.invoke(
                                CurrencyResult.Error(
                                    error = it
                                )
                            )
                        }
                    }
                }

                onFailure {
                    callback.invoke(
                        CurrencyResult.Error(
                            throwable = it
                        )
                    )
                }
            }
        }
    }

    /**
     * Fetches the live rate for all the currency from Currency API.
     * @param callback provides callback with api response
     */
    fun fetchLiveRateForSourceCurrency(callback: (CurrencyResult<LiveRatesCurrencyModel>) -> Unit) {
        if (!baseUrl.isNullOrBlank()) {
            Network.call(
                LiveCurrencyRateEndpoint(url = baseUrl!!, token = ACCESS_TOKEN)
            ) {
                onSuccess {
                    when (it) {
                        is EndpointResult.Response -> {
                            when (val decodedData = it.decodeResponse<LiveRatesCurrencyModel>()) {
                                is EndpointResult.Decoded.Value -> {
                                    callback.invoke(
                                        CurrencyResult.Value(
                                            data = decodedData.value
                                        )
                                    )
                                }

                                is EndpointResult.Decoded.Error -> {
                                    callback.invoke(
                                        CurrencyResult.Error(
                                            throwable = IllegalStateException(INVALID_RESPONSE_ERROR)
                                        )
                                    )
                                }
                            }
                        }

                        is EndpointResult.Error -> {
                            callback.invoke(
                                CurrencyResult.Error(
                                    error = it
                                )
                            )
                        }
                    }
                }

                onFailure {
                    callback.invoke(
                        CurrencyResult.Error(
                            throwable = it
                        )
                    )
                }
            }
        }
    }
}