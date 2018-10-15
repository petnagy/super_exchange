package com.petnagy.superexchange.pages.fragments.currentrate.viewmodel

import android.databinding.Bindable
import com.petnagy.superexchange.recyclerview.ListItemViewModel
import java.math.BigDecimal

/***
 * CurrentRateItem's ViewModel which is used in the RecyclerView.
 */
class CurrentRateItemViewModel(private val currency: String, private val rate: BigDecimal) : ListItemViewModel() {

    override fun areItemsTheSame(newItem: ListItemViewModel): Boolean {
        return this.currency == (newItem as CurrentRateItemViewModel).currency && this.rate == newItem.rate
    }

    override fun areContentsTheSame(newItem: ListItemViewModel): Boolean {
        return this.currency == (newItem as CurrentRateItemViewModel).currency && this.rate == newItem.rate
    }

    override fun getViewType() = 541235

    @Bindable
    fun getCurrencyName() = currency

    @Bindable
    fun getActualRate() = rate.toString()
}