package com.petnagy.superexchange.repository

import com.petnagy.superexchange.data.HistoryRate
import com.petnagy.superexchange.room.HistoryRateDao
import io.reactivex.Completable
import io.reactivex.Maybe
import timber.log.Timber

/***
 * Repository where we are caching the data into the room database.
 */
class HistoryRateRoomRepository(private val historyRateDao: HistoryRateDao) : Repository<HistoryRate> {

    override fun load(specification: Specification): Maybe<HistoryRate> {
        return if (specification !is HistoryRateSpecification) {
            Maybe.error(IllegalArgumentException("Wrong specification"))
        } else {
            Timber.d("baseCurrency = ${specification.base}, date = ${specification.date}")
            historyRateDao.query(specification.base, specification.date)
        }
    }

    override fun save(item: HistoryRate): Completable {
        Timber.d("HistoryRate save into room baseCurrency = ${item.base}, date = ${item.date}")
        return Completable.fromAction { historyRateDao.insert(item) }.doOnError { error -> Timber.e(error) }
    }

}