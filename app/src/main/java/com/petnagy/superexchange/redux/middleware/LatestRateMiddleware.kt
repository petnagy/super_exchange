package com.petnagy.superexchange.redux.middleware

import android.annotation.SuppressLint
import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Middleware
import com.petnagy.koredux.Store
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.redux.action.CalculateRatesAction
import com.petnagy.superexchange.redux.action.NetworkErrorAction
import com.petnagy.superexchange.redux.action.SetBaseCurrencyAction
import com.petnagy.superexchange.redux.action.SetLatestRateAction
import com.petnagy.superexchange.redux.state.AppState
import com.petnagy.superexchange.repository.LatestRateSpecification
import com.petnagy.superexchange.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class LatestRateMiddleware(private val repository: Repository<LatestRate>): Middleware<AppState> {

    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
        when(action) {
            is SetBaseCurrencyAction -> queryLatestRate(store)
            is CalculateRatesAction -> queryLatestRate(store)
        }
        next.dispatch(action)
    }

    @SuppressLint("CheckResult")
    private fun queryLatestRate(store: Store<AppState>) {
        val symbols = Currency.values().joinToString(",")
        //Because of free plan supported only EUR currency like a base currency
        val baseCurrency = "EUR"
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        repository.load(LatestRateSpecification(symbols = symbols, base = baseCurrency, date = date))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { latestRate -> handleLatestRate(store, latestRate)} ,
                        { error -> handleError(store, error)}
                )
    }

    private fun handleLatestRate(store: Store<AppState>, latestRate: LatestRate) {
        store.dispatch(SetLatestRateAction(latestRate))
    }

    private fun handleError(store: Store<AppState>, error: Throwable) {
        Timber.e(error, "Something went wrong in Network call")
        store.dispatch(NetworkErrorAction())
    }

}