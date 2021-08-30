package com.avinash.paypay.test.currencyconverter.viewmodel

import androidx.lifecycle.viewModelScope
import com.avinash.paypay.test.currencyconverter.base.IBaseViewModel
import com.avinash.paypay.test.currencyconverter.database.CurrencyDao
import com.avinash.paypay.test.currencyconverter.database.CurrencyEntity
import com.avinash.paypay.test.currencyconverter.viewintent.CurrencyIntent
import com.avinash.paypay.test.currencyconverter.viewstate.CurrencyState
import com.avinash.paypay.test.currencykotlin.datasource.CurrencyDataSource
import com.avinash.paypay.test.currencykotlin.model.CurrencyData
import com.avinash.paypay.test.currencykotlin.model.CurrencyResult
import com.avinash.paypay.test.currencykotlin.model.LiveRatesCurrencyModel
import com.avinash.paypay.test.foundation.logging.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
            is CurrencyIntent.FetchSupportedCurrency -> {
                fetchSupportedCountries()
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
    private fun fetchSupportedCountries() {
        viewModelScope.launch(Dispatchers.Default) {
            val currencies = currencyDao.allCurrencies()
            if (currencies.firstOrNull()?.currencyName.isNullOrBlank()) {
                    CurrencyDataSource.fetchSupportedCurrencies { result ->
                        if (result is CurrencyResult.Value) {
                            // process success
                            updateCurrencies(data = result.data?.currencyList)
                        }
                    }
            } else {
                updateLiveData(
                    CurrencyState.DisplaySupportedCurrency(
                        currencyData = currencies
                    )
                )
            }
        }
    }

    /**
     * Provides list of currency's live conversion rate compared to USD
     * either from database (if available) OR from API
     */
    private fun fetchLiveCurrencyRates() {
        viewModelScope.launch(Dispatchers.Default) {
            if (currencyDao.allCurrencies().isNullOrEmpty()) {
                    CurrencyDataSource.fetchLiveRateForSourceCurrency { result ->
                        if (result is CurrencyResult.Value) {
                            // process success
                            saveCurrencies(data = result.data)
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
        viewModelScope.launch(Dispatchers.Default) {
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

            dispatchIntent(CurrencyIntent.DisplayCurrencyConverters)
        }
    }

    /**
     * Updates list of currencies in our database with its country name
     * @param data list of currencies with country name which needs to be updated
     */
    private fun updateCurrencies(data: List<CurrencyData>?) {
        if (!data.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.Default) {
                data.forEach {
                    currencyDao.updateCurrency(
                        name = it.currencyCountryName,
                        code = it.currencyCode
                    )
                }

                val currencies = currencyDao.allCurrencies()
                updateLiveData(
                    CurrencyState.DisplaySupportedCurrency(
                        currencyData = currencies
                    )
                )
            }
        }
    }

    /**
     * Perform necessary task to set up currency converter tiles on converter screen
     */
    private fun displayCurrencyConverter() {
        viewModelScope.launch(Dispatchers.Default) {
            if (selectedCurrency == null) {
                selectedCurrency = currencyDao.allCurrencies().firstOrNull()
            }

            sourceCurrency = currencyDao.allCurrencies().firstOrNull { it.currencyCode == "USD" }

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