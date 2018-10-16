package com.petnagy.superexchange.pages.fragments.history.viewmodel

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.petnagy.superexchange.convert.RateConverter
import com.petnagy.superexchange.redux.action.LoadHistoryAction
import com.petnagy.superexchange.redux.state.AppState
import timber.log.Timber

class HistoryViewModel(private val store: Store<AppState>, private val converter: RateConverter) : ViewModel(), LifecycleObserver, StoreSubscriber<AppState> {

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

    override fun newState(state: AppState) {

    }

}