package com.avinash.paypay.test.currencyconverter.viewmodel

import androidx.lifecycle.viewModelScope
import com.avinash.paypay.test.currencyconverter.base.IBaseViewModel
import com.avinash.paypay.test.currencyconverter.database.CurrencyDao
import com.avinash.paypay.test.currencyconverter.database.CurrencyEntity
import com.avinash.paypay.test.currencyconverter.viewintent.CurrencyIntent
import com.avinash.paypay.test.currencyconverter.viewstate.CurrencyState
import com.avinash.paypay.test.currencykotlin.datasource.CurrencyDataSource
import com.avinash.paypay.test.currencykotlin.model.CurrencyResult
import com.avinash.paypay.test.currencykotlin.model.LiveRatesCurrencyModel
import com.avinash.paypay.test.foundation.logging.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import java.util.Currency
import javax.inject.Inject

/**
 * ViewModel class used to perform business logic for currency and trigger event
 * to update view with correct UI state
 */
@HiltViewModel
class CurrencyViewModel @Inject constructor(private val currencyDao: CurrencyDao) :
    IBaseViewModel<CurrencyIntent, CurrencyState>() {

    private var selectedCurrency: CurrencyEntity? = null
    private var sourceCurrency: CurrencyEntity? = null

    /**
     * Handles action OR events triggered by view which is then used to perform some task and provide
     * updated state back to the view using LiveData
     */
    override suspend fun handleIntents(intent: CurrencyIntent) {
        when (intent) {
            is CurrencyIntent.DisplayCurrencyList -> {
                displayCurrencyList()
            }

            is CurrencyIntent.FetchLiveCurrencyRate -> {
                fetchLiveCurrencyRates()
            }

            is CurrencyIntent.DisplayCurrencyConverters -> {
                displayCurrencyConverter()
            }

            is CurrencyIntent.UpdateCurrencySelection -> {
                selectedCurrency = intent.item
                updateLiveData(CurrencyState.UpdateCurrencySelection)
            }
        }
    }

    /**
     * Function which converts the entered amount for a particular currency
     * into equivalent USD and provide updated value to
     */
    fun updateConvertedAmountInUSD(value: String) {
        val convertedAmount = if(value.isNotBlank()) {
            convertAmount(
                amountToConvert = value.toDouble(),
                currencyRateForUSD = selectedCurrency?.currencyValue
            )
        } else {
            ""
        }

        updateLiveData(CurrencyState.UpdateConvertedAmount(convertedAmount = convertedAmount))
    }

    /**
     * Provides supported country currency list either from database if available OR from API
     */
    private fun displayCurrencyList() {
        viewModelScope.launch {
            val currencies = currencyDao.allCurrencies()
            updateLiveData(
                CurrencyState.DisplaySupportedCurrency(
                    currencyData = currencies
                )
            )
        }
    }

    /**
     * Provides list of currency's live conversion rate compared to USD
     * either from database (if available) OR from API
     */
    private fun fetchLiveCurrencyRates() {
        viewModelScope.launch {
            if (currencyDao.allCurrencies().isNullOrEmpty()) {
                    CurrencyDataSource.fetchLiveRateForSourceCurrency { result ->
                        // process success result
                        if (result is CurrencyResult.Value) {
                            // Save currencies in database
                            saveCurrencies(data = result.data)

                            // Update currency converter screen with below data for first launch
                            if (selectedCurrency == null) {
                                result.data?.currencyQuotes?.firstOrNull()?.let {
                                    selectedCurrency = CurrencyEntity(
                                        currencyCode =  it.currencyCode,
                                        currencyValue = it.currencyRate.toDouble()
                                    )
                                }
                            }

                            result.data?.currencyQuotes?.firstOrNull { it.currencyCode == USD_CURRENCY }?.let {
                                sourceCurrency = CurrencyEntity(
                                    currencyCode =  it.currencyCode,
                                    currencyValue = it.currencyRate.toDouble()
                                )
                            }

                            updateLiveData(
                                CurrencyState.DisplayCurrencyConverter(
                                    selectedCurrency = selectedCurrency,
                                    sourceCurrency = sourceCurrency
                                )
                            )
                        }
                    }
            } else {
                dispatchIntent(CurrencyIntent.DisplayCurrencyConverters)
            }
        }
    }

    /**
     * Saves currencies fetched from API in our currency database
     * @param data list of currencies which needs to be stored in our database
     */
    private fun saveCurrencies(data: LiveRatesCurrencyModel?) {
        viewModelScope.launch {
            val currencies = data?.currencyQuotes?.map {
                CurrencyEntity(
                    currencyCode = it.currencyCode,
                    currencyValue = it.currencyRate.toDouble()
                )
            }

            currencyDao.deleteCurrencies()
            if (!currencies.isNullOrEmpty()) {
                currencyDao.insertCurrencies(currencies = currencies)
            }
        }
    }

    /**
     * Perform necessary task to set up currency converter tiles on converter screen
     */
    private fun displayCurrencyConverter() {
        viewModelScope.launch {
            if (selectedCurrency == null) {
                selectedCurrency = currencyDao.allCurrencies().firstOrNull()
            }

            sourceCurrency = currencyDao.allCurrencies().firstOrNull { it.currencyCode == USD_CURRENCY }

            updateLiveData(
                CurrencyState.DisplayCurrencyConverter(
                    selectedCurrency = selectedCurrency,
                    sourceCurrency = sourceCurrency
                )
            )
        }
    }

    override fun onCleared() {
        viewModelScope.coroutineContext.cancelChildren()
        super.onCleared()
    }

    companion object {
        private const val USD_CURRENCY = "USD"
        private val decimalFormatter by lazy {
            val numberFormatter = NumberFormat.getNumberInstance(Locale.US)
            numberFormatter.maximumFractionDigits = 2
            numberFormatter.currency = Currency.getInstance(Locale.US)
            numberFormatter
        }

        fun convertAmount(amountToConvert: Double?, currencyRateForUSD: Double?): String {
            return if (currencyRateForUSD != null && amountToConvert != null) {
                val amountValue = (amountToConvert / currencyRateForUSD)
                Log.debug("Converted value = $amountValue")
                decimalFormatter.format(amountValue)
            } else {
                ""
            }
        }
    }
}