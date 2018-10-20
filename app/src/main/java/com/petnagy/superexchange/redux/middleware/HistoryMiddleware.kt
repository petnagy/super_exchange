package com.petnagy.superexchange.redux.middleware

import android.annotation.SuppressLint
import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Middleware
import com.petnagy.koredux.Store
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.HistoryRate
import com.petnagy.superexchange.redux.action.HistoryErrorAction
import com.petnagy.superexchange.redux.action.LoadHistoryAction
import com.petnagy.superexchange.redux.action.SetHistoryListAction
import com.petnagy.superexchange.redux.state.AppState
import com.petnagy.superexchange.repository.HistoryRateSpecification
import com.petnagy.superexchange.repository.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class HistoryMiddleware(private val historyRateRepo: Repository<HistoryRate>) : Middleware<AppState> {

    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
        when (action) {
            is LoadHistoryAction -> loadFromNet(store)
        }
        next.dispatch(action)
    }

    @SuppressLint("CheckResult")
    private fun loadFromNet(store: Store<AppState>) {
        Observable.range(1, 7)
                .map { day -> createDate(day) }
                .flatMap { date -> historyRateRepo.load(HistoryRateSpecification(Currency.values().joinToString(","), "EUR", date)).toObservable() }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { resultList -> handleHistoryRateList(store, resultList) },
                        { error -> handleError(store, error) }
                )
    }

    private fun handleHistoryRateList(store: Store<AppState>, resultList: List<HistoryRate>) {
        resultList.forEach { result -> Timber.d(result.toString()) }
        store.dispatch(SetHistoryListAction(resultList))
    }

    private fun handleError(store: Store<AppState>, error: Throwable) {
        Timber.e(error, "Something went wrong in load HistoryRate")
        store.dispatch(HistoryErrorAction())
    }

    private fun createDate(day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, day * -1)
        return SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
    }
}