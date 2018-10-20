package com.petnagy.superexchange.pages.fragments.history.viewmodel

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.view.View
import android.widget.AdapterView
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.petnagy.superexchange.convert.RateConverter
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.extensions.default
import com.petnagy.superexchange.redux.action.BaseCurrencyChangedAction
import com.petnagy.superexchange.redux.action.LoadHistoryAction
import com.petnagy.superexchange.redux.state.AppState
import com.petnagy.superexchange.redux.state.FragmentState
import timber.log.Timber

class HistoryViewModel(private val store: Store<AppState>, private val rateConverter: RateConverter) : ViewModel(), LifecycleObserver, StoreSubscriber<AppState> {

    val loading = MutableLiveData<Boolean>()
    val baseCurrency = MutableLiveData<String>()
    val currenciesList = Currency.values().map { currency -> currency.name }.toList()
    val rates = MutableLiveData<List<HistoryItemViewModel>>().default(emptyList())

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        store.subscribe(this)
        Timber.d("HistoryViewModel start")
        store.dispatch(LoadHistoryAction())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        store.unsubscribe(this)
        Timber.d("HistoryViewModel stop")
    }

    fun getSelectedListener() = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            // Do nothing
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val currencyName = currenciesList[position]
            store.dispatch(BaseCurrencyChangedAction(Currency.valueOf(currencyName)))
        }
    }

    override fun newState(state: AppState) {
        loading.value = state.historyRateState.loading
        baseCurrency.value = state.historyRateState.baseCurrency?.name
        if (state.historyRateState.rates != null && state.historyRateState.baseCurrency != null) {
            rates.value = rateConverter.convertHistoryItems(state.historyRateState.rates, state.historyRateState.baseCurrency.name)
        } else {
            rates.value = emptyList()
        }
        // Unsubscribe if current fragment is not HistoryRate...
        if (state.fragmentState != FragmentState.HISTORY) {
            store.unsubscribe(this)
        }
    }
}
