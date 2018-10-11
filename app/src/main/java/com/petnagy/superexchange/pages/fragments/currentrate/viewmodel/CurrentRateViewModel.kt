package com.petnagy.superexchange.pages.fragments.currentrate.viewmodel

import android.arch.lifecycle.*
import com.petnagy.superexchange.data.Country
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.extensions.default
import com.petnagy.superexchange.pages.fragments.currentrate.model.CurrentRateModel
import com.petnagy.superexchange.permission.PermissionStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.math.RoundingMode

/***
 * Current Rate related ViewModel.
 */
class CurrentRateViewModel(private val model: CurrentRateModel) : ViewModel(), LifecycleObserver {

    val loading = MutableLiveData<Boolean>()
    val baseCurrency = MutableLiveData<String>()
    val currenciesList = Currency.values().map { currency -> currency.name }.toList()
    val rates = MutableLiveData<List<CurrentRateItemViewModel>>().default(emptyList())
    private lateinit var countryDisposable: Disposable
    private lateinit var latestRateDisposable: Disposable
    val fineLocationPermissionStatus: MutableLiveData<PermissionStatus> = MutableLiveData<PermissionStatus>().default(PermissionStatus.PERMISSION_DENIED)

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        loading.value = true
        Timber.d("Country search started")
        countryDisposable = model.getBaseCurrency(fineLocationPermissionStatus.value
                ?: PermissionStatus.PERMISSION_DENIED)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose { Timber.d("Country search is disposed") }
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { result -> processSelectedCurrency(result) },
                        { error -> processCurrencyError(error) }
                )
    }

    private fun processSelectedCurrency(countryCode: String) {
        Timber.d("Country result: $countryCode")
        val country = Country.values().find { country -> country.name == countryCode }
        if (country != null) {
            baseCurrency.value = country.currency.name
            latestRateDisposable = model.queryCurrentRate()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnDispose { Timber.d("Latest rate is disposed") }
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            { result -> processLatestRate(result) },
                            { error -> processLatestRateError(error) }
                    )
        } else {
            //TODO show error dialog
            loading.value = false
        }
    }

    private fun processLatestRate(result: LatestRate) {
        Timber.d("LatestRate is arrived: $result")
        loading.value = false
        val baseRate = result.rates[baseCurrency.value]
        baseRate?.let { rate ->
            val convertedMap = result.rates.mapValues { entry ->
                entry.value.divide(rate, 6, RoundingMode.CEILING)
            }
            Timber.d("Converted map: $convertedMap")
            rates.value = convertedMap.map { entry -> CurrentRateItemViewModel(entry.key, entry.value) }.toList()
        }
    }

    private fun processLatestRateError(error: Throwable) {
        Timber.d(error, "Error happened")
        loading.value = false
    }

    private fun processCurrencyError(error: Throwable) {
        Timber.e(error, "Error")
        loading.value = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Timber.d("CurrentRateViewModel stop")
        if (!countryDisposable.isDisposed) {
            countryDisposable.dispose()
        }
    }
}