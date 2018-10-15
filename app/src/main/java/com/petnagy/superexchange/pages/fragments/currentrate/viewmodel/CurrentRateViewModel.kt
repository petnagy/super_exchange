package com.petnagy.superexchange.pages.fragments.currentrate.viewmodel

import android.arch.lifecycle.*
import android.view.View
import android.widget.AdapterView
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.petnagy.superexchange.convert.RateConverter
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.extensions.default
import com.petnagy.superexchange.location.LatestRateStatus
import com.petnagy.superexchange.permission.PermissionStatus
import com.petnagy.superexchange.redux.action.CalculateRatesAction
import com.petnagy.superexchange.redux.action.SetBaseCurrencyAction
import com.petnagy.superexchange.redux.action.StartLocationSearchAction
import com.petnagy.superexchange.redux.state.AppState
import timber.log.Timber

/***
 * Current Rate related ViewModel.
 */
class CurrentRateViewModel(private val store: Store<AppState>, private val rateConverter: RateConverter) : ViewModel(), LifecycleObserver, StoreSubscriber<AppState> {

    val loading = MutableLiveData<Boolean>()
    val baseCurrency = MutableLiveData<String>()
    val currenciesList = Currency.values().map { currency -> currency.name }.toList()
    val rates = MutableLiveData<List<CurrentRateItemViewModel>>().default(emptyList())
    val fineLocationPermissionStatus: MutableLiveData<PermissionStatus> = MutableLiveData<PermissionStatus>().default(PermissionStatus.PERMISSION_DENIED)
    val status: MutableLiveData<LatestRateStatus> = MutableLiveData<LatestRateStatus>().default(LatestRateStatus.STATUS_UNKNOWN)
    val amount: MediatorLiveData<Int> = MediatorLiveData<Int>().default(1)

    override fun newState(state: AppState) {
        loading.value = state.latestRateState.loading
        if (baseCurrencyEmptyOrChanged(state)) {
            baseCurrency.value = state.latestRateState.baseCurrency?.name
        }
        amount.value = state.latestRateState.amount
        status.value = state.latestRateState.status
        if (state.latestRateState.latestRate != null && state.latestRateState.baseCurrency != null) {
            rates.value = rateConverter.convertLatestRate(state.latestRateState.latestRate, state.latestRateState.baseCurrency.name, state.latestRateState.amount)
        } else {
            rates.value = emptyList()
        }
    }

    private fun baseCurrencyEmptyOrChanged(state: AppState) =
            baseCurrency.value == null || baseCurrency.value != state.latestRateState.baseCurrency?.name

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        store.subscribe(this)
        baseCurrency.value = null
        store.dispatch(StartLocationSearchAction(fineLocationPermissionStatus.value
                ?: PermissionStatus.PERMISSION_DENIED))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        store.unsubscribe(this)
        Timber.d("CurrentRateViewModel stop")
    }

    fun getSelectedListener() = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            //Do nothing
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val currency = currenciesList[position]
            store.dispatch(SetBaseCurrencyAction(Currency.valueOf(currency)))
        }
    }

    fun onCalculatePressed(view: View) {
        Timber.d("Calculate button pressed")
        store.dispatch(CalculateRatesAction(amount.value ?: 1))
    }
}