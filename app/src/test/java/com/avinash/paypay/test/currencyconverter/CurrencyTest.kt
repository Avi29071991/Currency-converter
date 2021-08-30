package com.avinash.paypay.test.currencyconverter

import com.avinash.paypay.test.currencyconverter.viewmodel.CurrencyViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyTest {

    @Test
    fun `test currency conversion to USD`() {
        val amountToConvert = 10000.0

        var currencyRate = 3.67301 // AED conversion rate
        assertEquals(CurrencyViewModel.convertAmount(amountToConvert, currencyRate), "2,722.56")

        currencyRate = 0.726825 // GBP conversion rate
        assertEquals(CurrencyViewModel.convertAmount(amountToConvert, currencyRate), "13,758.47")

        currencyRate = 14339.0 // IDR conversion rate
        assertEquals(CurrencyViewModel.convertAmount(amountToConvert, currencyRate), "0.7")

        currencyRate = 73.288001 // INR conversion rate
        assertEquals(CurrencyViewModel.convertAmount(amountToConvert, currencyRate), "136.45")

        currencyRate = 109.871983 // JPY conversion rate
        assertEquals(CurrencyViewModel.convertAmount(amountToConvert, currencyRate), "91.02")
    }
}