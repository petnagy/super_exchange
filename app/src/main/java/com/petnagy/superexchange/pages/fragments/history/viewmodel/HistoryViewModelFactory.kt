package com.petnagy.superexchange.pages.fragments.history.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.petnagy.koredux.Store
import com.petnagy.superexchange.convert.RateConverter
import com.petnagy.superexchange.redux.state.AppState

class HistoryViewModelFactory constructor(private val store: Store<AppState>, private val rateConverter: RateConverter) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(store, rateConverter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}