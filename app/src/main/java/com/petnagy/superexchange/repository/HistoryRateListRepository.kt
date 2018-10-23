package com.petnagy.superexchange.repository

import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.HistoryRate
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.Calendar

/***
 * This repository will provide an Observable which will call HistoryRateCompositeRepository
 */
class HistoryRateListRepository(private val repository: Repository<HistoryRate>) : Repository<List<HistoryRate>> {

    companion object {
        private const val DAY_OF_START = 1
        private const val DAYS_IN_PAST = 7
    }

    override fun load(specification: Specification): Maybe<List<HistoryRate>> {
        return Observable.range(DAY_OF_START, DAYS_IN_PAST)
                .map { day -> createDate(day) }
                .flatMap { date -> repository.load(HistoryRateSpecification(Currency.values().joinToString(","), "EUR", date)).toObservable() }
                .toList()
                .toMaybe()
    }

    override fun save(item: List<HistoryRate>): Completable {
        // Do not call this method!
        return Completable.never()
    }

    private fun createDate(day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, day * -1)
        return SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
    }
}
