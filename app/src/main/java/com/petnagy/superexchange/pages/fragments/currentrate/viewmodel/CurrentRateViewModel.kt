package com.petnagy.superexchange.pages.fragments.currentrate.viewmodel

import android.arch.lifecycle.*
import com.petnagy.superexchange.data.Currencies
import timber.log.Timber

/***
 * Current Rate related ViewModel.
 */
class CurrentRateViewModel: ViewModel(), LifecycleObserver {

    val loading = MutableLiveData<Boolean>()
    val currenciesList = Currencies.values().map { currency -> currency.name }.toList()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        Timber.d("CurrentRateViewModel start")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Timber.d("CurrentRateViewModel stop")
    }
}