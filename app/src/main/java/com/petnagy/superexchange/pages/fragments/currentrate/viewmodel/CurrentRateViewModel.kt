package com.petnagy.superexchange.pages.fragments.currentrate.viewmodel

import android.arch.lifecycle.*
import com.petnagy.superexchange.data.Currencies
import com.petnagy.superexchange.pages.fragments.currentrate.model.CurrentRateModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/***
 * Current Rate related ViewModel.
 */
class CurrentRateViewModel(private val model: CurrentRateModel): ViewModel(), LifecycleObserver {

    val loading = MutableLiveData<Boolean>()
    val baseCurrency = MutableLiveData<String>()
    val currenciesList = Currencies.values().map { currency -> currency.name }.toList()
    lateinit var countryDisposable: Disposable

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        Timber.d("CurrentRateViewModel start")
        countryDisposable = model.getBaseCurrency()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose { Timber.d("country query is disposed") }
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { result -> processSelectedCurrency(result) },
                        { error -> processCurrencyError(error) }
                )
    }

    private fun processSelectedCurrency(result: String) {
        baseCurrency.value = result
    }

    private fun processCurrencyError(error: Throwable) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Timber.d("CurrentRateViewModel stop")
        if (!countryDisposable.isDisposed) {
            countryDisposable.dispose()
        }
    }
}