package com.avinash.paypay.test.currencyconverter.supportedcountries.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avinash.paypay.test.currencyconverter.database.CurrencyEntity
import com.avinash.paypay.test.currencyconverter.databinding.CurrencyRowBinding

/**
 * Adapter class used to display list of currencies for supported countries
 */
class CurrencyAdapter(
    private val currencies: List<CurrencyEntity>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    /**
     * Interface to provide mechanism for recycler view item's click listener
     */
    interface OnItemClickListener {
        /**
         * Click action on an item in recycler view to provide selected [CurrencyEntity]
         * @param item selected [CurrencyEntity]
         */
        fun onClick(item: CurrencyEntity)
    }

    /**
     * [RecyclerView.ViewHolder] class used to provide list item view for Currency list
     */
    inner class CurrencyViewHolder(val binding: CurrencyRowBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = CurrencyRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return CurrencyViewHolder(binding)
    }

    // Bind the items with each item of the list currency which than will be
    // shown in recycler view
    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        with(holder) {
            with(currencies[position]) {
                binding.currencyCode.text = this.currencyCode
                if (!this.currencyName.isNullOrBlank()) {
                    binding.currenyName.text = this.currencyName
                }

                if (this.currencyValue != null) {
                    binding.currenyValue.text = this.currencyValue.toString()
                }

                binding.root.setOnClickListener {
                    listener.onClick(this)
                }
            }
        }
    }

    // return the size of currency list
    override fun getItemCount(): Int {
        return currencies.size
    }
}