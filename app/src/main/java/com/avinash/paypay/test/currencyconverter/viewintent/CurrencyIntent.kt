package com.avinash.paypay.test.currencyconverter.viewintent

import com.avinash.paypay.test.currencyconverter.base.ViewIntent
import com.avinash.paypay.test.currencyconverter.database.CurrencyEntity

/**
 * Intent OR Action used to perform certain task for currency screens.
 * These actions will be provided to ViewModel which will process it and then
 * provide updated UI state to the view
 */
sealed class CurrencyIntent: ViewIntent {

    /**
     * Action used to fetch supported currency list and display it to the user on screen
     * This list will either be fetched from Database
     */
    object DisplayCurrencyList: CurrencyIntent()

    /**
     * Action used to fetch currency live rate list which will help us to convert one currency to USD
     * This list will either be fetched from API OR Database(if available)
     */
    object FetchLiveCurrencyRate: CurrencyIntent()

    /**
     * Action used to display the currency converter tiles.
     * This action indicates that the data to be shown to the user is available
     */
    object DisplayCurrencyConverters: CurrencyIntent()

    /**
     * Action used to indicate that the currency selection which needs to be converted is changed
     * @param item Selected [CurrencyEntity] which needs to be converted to equivalent USD
     */
    data class UpdateCurrencySelection(val item: CurrencyEntity): CurrencyIntent()
}
