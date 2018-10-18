package com.petnagy.superexchange.pages.fragments.history.viewmodel

import android.databinding.Bindable
import com.petnagy.superexchange.recyclerview.ListItemViewModel

class HistoryItemViewModel(private val date: String, private val rates: String) : ListItemViewModel() {

    override fun areItemsTheSame(newItem: ListItemViewModel): Boolean {
        return this.date == (newItem as HistoryItemViewModel).date && this.rates == newItem.rates
    }

    override fun areContentsTheSame(newItem: ListItemViewModel): Boolean {
        return this.date == (newItem as HistoryItemViewModel).date && this.rates == newItem.rates
    }

    override fun getViewType() = 541235

    @Bindable
    fun getDate() = date

    @Bindable
    fun getRates() = rates
}