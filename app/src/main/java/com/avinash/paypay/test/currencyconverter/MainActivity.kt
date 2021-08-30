package com.avinash.paypay.test.currencyconverter

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.avinash.paypay.test.currencyconverter.base.IBaseActivity
import com.avinash.paypay.test.currencyconverter.databinding.ActivityMainBinding
import com.avinash.paypay.test.currencyconverter.viewintent.CurrencyIntent
import com.avinash.paypay.test.currencyconverter.viewmodel.CurrencyViewModel
import com.avinash.paypay.test.currencyconverter.viewstate.CurrencyState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : IBaseActivity<ActivityMainBinding,
        CurrencyIntent,
        CurrencyState,
        CurrencyViewModel>() {



    override val viewModel by viewModels<CurrencyViewModel>()

    override fun setViewBinding(
        layoutInflater: LayoutInflater
    ) = ActivityMainBinding.inflate(layoutInflater)

    override fun render(state: CurrencyState) {
        // No Implementation is required
    }
}