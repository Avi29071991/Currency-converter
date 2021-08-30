package com.avinash.paypay.test.currencyconverter.supportedcountries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.avinash.paypay.test.currencyconverter.base.IBaseFragment
import com.avinash.paypay.test.currencyconverter.database.CurrencyEntity
import com.avinash.paypay.test.currencyconverter.databinding.FragmentCountrySelectionBinding
import com.avinash.paypay.test.currencyconverter.supportedcountries.adapter.CurrencyAdapter
import com.avinash.paypay.test.currencyconverter.viewintent.CurrencyIntent
import com.avinash.paypay.test.currencyconverter.viewmodel.CurrencyViewModel
import com.avinash.paypay.test.currencyconverter.viewstate.CurrencyState

/**
 * Fragment used to display supported countries with its currency code in a list
 */
class CountriesFragment: IBaseFragment<FragmentCountrySelectionBinding,
        CurrencyIntent,
        CurrencyState,
        CurrencyViewModel>() {

    // Sharing the same view model for fragments residing in the same activity
    override val viewModel by activityViewModels<CurrencyViewModel>()

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCountrySelectionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dispatchIntent(CurrencyIntent.FetchSupportedCurrency)
    }

    /**
     * Initialising adapter class for displaying list of currencies
     * @param currencyList List of [CurrencyEntity] to be displayed in list
     */
    private fun initAdapter(currencyList: List<CurrencyEntity>?) {
        if (!currencyList.isNullOrEmpty()) {
            viewBinding.countryList.adapter = CurrencyAdapter(
                currencies = currencyList,
                listener = object : CurrencyAdapter.OnItemClickListener {
                    override fun onClick(item: CurrencyEntity) {
                        viewModel.dispatchIntent(
                            CurrencyIntent.UpdateCurrencySelection(item = item)
                        )
                    }
                }
            )

            viewBinding.countryList.visibility = View.VISIBLE
            viewBinding.progressCircular.visibility = View.GONE
        }
    }

    /**
     * Renders different [CurrencyState] state of the currency list screen
     * This will help us to display correct UI as per the state available
     */
    override fun render(state: CurrencyState) {
        when(state) {
            is CurrencyState.DisplaySupportedCurrency -> {
                initAdapter(currencyList = state.currencyData)
            }

            is CurrencyState.UpdateCurrencySelection -> {
                findNavController().navigateUp()
            }

            else -> Unit
        }
    }
}