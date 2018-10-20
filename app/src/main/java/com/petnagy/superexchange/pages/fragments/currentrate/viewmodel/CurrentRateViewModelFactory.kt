package com.petnagy.superexchange.pages.fragments.currentrate.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.petnagy.koredux.Store
import com.petnagy.superexchange.convert.RateConverter
import com.petnagy.superexchange.redux.state.AppState

/***
 * ViewModelProvider.Factory class for {@link CurrentRateViewModel}, because it has no any default constructor.
 */
class CurrentRateViewModelFactory constructor(private val store: Store<AppState>, private val rateConverter: RateConverter) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentRateViewModel::class.java)) {
            return CurrentRateViewModel(store, rateConverter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}