package com.petnagy.superexchange.pages.fragments.currentrate.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.petnagy.superexchange.convert.RateConverter
import com.petnagy.superexchange.pages.fragments.currentrate.model.CurrentRateModel

/***
 * ViewModelProvider.Factory class for {@link CurrentRateViewModel}, because it has no any default constructor.
 */
class CurrentRateViewModelFactory constructor(private val currentRateModel: CurrentRateModel, private val rateConverter: RateConverter) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentRateViewModel::class.java)) {
            return CurrentRateViewModel(currentRateModel, rateConverter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}