package com.petnagy.superexchange.pages.fragments.currentrate.viewmodel

import android.databinding.Bindable
import com.petnagy.superexchange.recyclerview.ListItemViewModel
import java.math.BigDecimal

class CurrentRateItemViewModel(private val currency: String, private val rate: BigDecimal) : ListItemViewModel() {

    override fun areItemsTheSame(newItem: ListItemViewModel): Boolean {
        return this.currency == (newItem as CurrentRateItemViewModel).currency
    }

    override fun areContentsTheSame(newItem: ListItemViewModel): Boolean {
        return this.currency == (newItem as CurrentRateItemViewModel).currency
    }

    override fun getViewType() = 541235

    @Bindable
    fun getCurrencyName() = currency

    @Bindable
    fun getActualRate() = rate.toString()
}