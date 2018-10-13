package com.petnagy.superexchange.pages.fragments.currentrate.viewmodel

import android.arch.lifecycle.*
import com.petnagy.superexchange.convert.RateConverter
import com.petnagy.superexchange.data.Country
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.errors.LocationSettingsException
import com.petnagy.superexchange.errors.NoPlayServiceException
import com.petnagy.superexchange.errors.PermissionDeniedException
import com.petnagy.superexchange.errors.PermissionNotGrantedException
import com.petnagy.superexchange.extensions.default
import com.petnagy.superexchange.location.LocationStatus
import com.petnagy.superexchange.pages.fragments.currentrate.model.CurrentRateModel
import com.petnagy.superexchange.permission.PermissionStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/***
 * Current Rate related ViewModel.
 */
class CurrentRateViewModel(private val model: CurrentRateModel, private val rateConverter: RateConverter) : ViewModel(), LifecycleObserver {

    val loading = MutableLiveData<Boolean>()
    val baseCurrency = MutableLiveData<String>()
    val currenciesList = Currency.values().map { currency -> currency.name }.toList()
    val rates = MutableLiveData<List<CurrentRateItemViewModel>>().default(emptyList())
    private lateinit var countryDisposable: Disposable
    private lateinit var latestRateDisposable: Disposable
    val fineLocationPermissionStatus: MutableLiveData<PermissionStatus> = MutableLiveData<PermissionStatus>().default(PermissionStatus.PERMISSION_DENIED)
    val status: MutableLiveData<LocationStatus> = MutableLiveData<LocationStatus>().default(LocationStatus.STATUS_OK)

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
            latestRateDisposable = model.queryCurrentRate(country.currency.name)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnDispose { Timber.d("Latest rate is disposed") }
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            { result -> processLatestRate(result) },
                            { error -> processLatestRateError(error) }
                    )
        } else {
            status.value = LocationStatus.LOCATION_ERROR
            loading.value = false
        }
    }

    private fun processLatestRate(result: LatestRate) {
        Timber.d("LatestRate is arrived: $result")
        loading.value = false
        rates.value = rateConverter.convertLatestRate(result, baseCurrency.value ?: "")
    }

    private fun processLatestRateError(error: Throwable) {
        Timber.d(error, "Something went wrong during get location chain")
        loading.value = false
    }

    private fun processCurrencyError(error: Throwable) {
        Timber.e(error, "Error")
        when(error) {
            is PermissionNotGrantedException -> status.value = LocationStatus.PERMISSION_NEED
            is NoPlayServiceException -> status.value = LocationStatus.PLAY_SERVICE_ERROR
            is LocationSettingsException -> status.value = LocationStatus.SETTING_ERROR
            is PermissionDeniedException -> status.value = LocationStatus.PERMISSION_DENIED
            else ->  status.value = LocationStatus.SETTING_ERROR
        }
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