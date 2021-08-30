package com.avinash.paypay.test.currencyconverter.viewstate

import com.avinash.paypay.test.currencyconverter.base.ViewState
import com.avinash.paypay.test.currencyconverter.database.CurrencyEntity

/**
 * UI state for currency screens which will help us to provide correct UI to the user
 */
sealed class CurrencyState: ViewState {

    /**
     * UI state used to indicate that currency list for supported countries needs to be displayed
     * @param currencyData List of [CurrencyEntity] which needs to be displayed to the user
     */
    data class DisplaySupportedCurrency(
        val currencyData: List<CurrencyEntity>?
    ) : CurrencyState()

    /**
     * UI State used to indicate that the currency converter tiles needs to be displayed to the user
     * @param selectedCurrency [CurrencyEntity] selected currency code for which equivalent USD needs to be calculated
     * @param sourceCurrency [CurrencyEntity] US currency code which is used to calculate equivalent USD amount
     */
    data class DisplayCurrencyConverter(
        val selectedCurrency: CurrencyEntity?,
        val sourceCurrency: CurrencyEntity?
    ) : CurrencyState()

    /**
     * UI State used to indicate that the selection currency code needs to be updated on converter screen
     */
    object UpdateCurrencySelection: CurrencyState()

    /**
     * UI state used to indicate that the entered amount is converted and the value is updated on converter screen
     * @param convertedAmount Equivalent USD amount which needs to be updated on converter screen
     */
    data class UpdateConvertedAmount(val convertedAmount: String= ""): CurrencyState()
}
