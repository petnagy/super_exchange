package com.petnagy.superexchange.repository

import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.room.LatestRateDao
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.functions.Predicate
import timber.log.Timber

/***
 * Repository where we are caching the data into the room database.
 */
class LatestRateRoomRepository(private val latestRateDao: LatestRateDao) : Repository<LatestRate> {

    companion object {
        private const val ONE_HOUR_IN_MILLI_SEC: Long = 60 * 60 * 1000
        private const val ONE_SEC_IN_MILLI_SEC: Long = 1000
    }

    override fun load(specification: Specification): Maybe<LatestRate> {
        return if (specification !is LatestRateSpecification) {
            Maybe.error(IllegalArgumentException("Wrong specification"))
        } else {
            Timber.d("baseCurrency = ${specification.base}, date = ${specification.date}")
            latestRateDao.query(specification.base, specification.date).filter { latestRate -> isDoNotCallTimeOver(latestRate = latestRate) }
        }
    }

    private fun isDoNotCallTimeOver(latestRate: LatestRate) = latestRate.timestamp + ONE_HOUR_IN_MILLI_SEC > System.currentTimeMillis().div(ONE_SEC_IN_MILLI_SEC)

    override fun save(item: LatestRate): Completable {
        Timber.d("Save into room baseCurrency = ${item.base}, date = ${item.date}")
        return Completable.fromAction { latestRateDao.insert(item) }.doOnError { error -> Timber.e(error) }
    }

}