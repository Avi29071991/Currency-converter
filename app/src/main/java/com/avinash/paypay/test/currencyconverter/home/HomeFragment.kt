package com.avinash.paypay.test.currencyconverter.home

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.avinash.paypay.test.currencyconverter.R
import com.avinash.paypay.test.currencyconverter.base.IBaseFragment
import com.avinash.paypay.test.currencyconverter.database.CurrencyEntity
import com.avinash.paypay.test.currencyconverter.databinding.FragmentHomeBinding
import com.avinash.paypay.test.currencyconverter.viewintent.CurrencyIntent
import com.avinash.paypay.test.currencyconverter.viewmodel.CurrencyViewModel
import com.avinash.paypay.test.currencyconverter.viewstate.CurrencyState

/**
 * Fragment which displays the currency converter sections.
 * Here every currency is converted to equivalent USD. The free API throws error
 * when source currency is provided as any currency code apart from USD.
 */
class HomeFragment: IBaseFragment<FragmentHomeBinding,
        CurrencyIntent,
        CurrencyState,
        CurrencyViewModel>() {

    // Sharing the same view model for fragments residing in the same activity
    override val viewModel by activityViewModels<CurrencyViewModel>()

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dispatchIntent(CurrencyIntent.FetchLiveCurrencyRate)
        viewBinding.selectedCurrencyCode.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_selectionFragment)
        }

        viewBinding.selectedCurrencyValue.filters = arrayOf<InputFilter>(LengthFilter(EDIT_TEXT_LENGTH_FILTER))
        viewBinding.selectedCurrencyValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not required
            }

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                val textValue = if (!text.isNullOrBlank()) {
                    text.toString()
                } else ""
                viewModel.updateConvertedAmountInUSD(value = textValue)
            }

            override fun afterTextChanged(s: Editable?) {
                // Not Required
            }
        })
    }

    /**
     * Renders different [CurrencyState] state of currency converter screen
     * This will help us to display correct UI as per the state available
     */
    override fun render(state: CurrencyState) {
        when(state) {
            is CurrencyState.DisplayCurrencyConverter -> {
                displayScreenItems(
                    selectedCurrency = state.selectedCurrency,
                    sourceCurrency = state.sourceCurrency
                )
            }

            is CurrencyState.UpdateConvertedAmount -> {
                viewBinding.sourceCurrencyValue.text = state.convertedAmount
            }

            else -> Unit
        }
    }

    /**
     * Display screen items on the screen when appropriate [CurrencyState.DisplayCurrencyConverter] render is called
     * @param selectedCurrency [CurrencyEntity] selected currency code for which equivalent USD needs to be calculated
     * @param sourceCurrency [CurrencyEntity] US currency code which is used to calculate equivalent USD amount
     */
    private fun displayScreenItems(
        selectedCurrency: CurrencyEntity?,
        sourceCurrency: CurrencyEntity?
    ) {
        viewBinding.progressCircular.visibility = View.GONE
        viewBinding.currencyContainer.visibility = View.VISIBLE

        if (!selectedCurrency?.currencyCode.isNullOrBlank()) {
            viewBinding.selectedCurrencyCode.text = selectedCurrency?.currencyCode
        }

        if (!sourceCurrency?.currencyCode.isNullOrBlank()) {
            viewBinding.sourceCurrencyCode.text = sourceCurrency?.currencyCode
        }
    }

    companion object {
        private const val EDIT_TEXT_LENGTH_FILTER = 9
    }
}