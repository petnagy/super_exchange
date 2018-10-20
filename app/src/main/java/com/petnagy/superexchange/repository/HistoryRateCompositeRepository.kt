package com.petnagy.superexchange.repository

import com.petnagy.superexchange.data.HistoryRate
import io.reactivex.Completable
import io.reactivex.Maybe
import timber.log.Timber

/***
 * Composite repository, it is using network and room repository at the case of HistoryRate.
 */
class HistoryRateCompositeRepository(
    private val networkRepo: HistoryRateNetworkRepository,
    private val roomRepo: HistoryRateRoomRepository
) : Repository<HistoryRate> {

    override fun load(specification: Specification): Maybe<HistoryRate> {
        return if (specification !is HistoryRateSpecification) {
            Maybe.error(IllegalArgumentException("Wrong specification"))
        } else {
            roomRepo.load(specification).doOnSuccess { Timber.d("Load from cache") }
                    .switchIfEmpty(networkRepo.load(specification)
                            .doAfterSuccess { latestRate -> roomRepo.save(latestRate).subscribe() })
        }
    }

    override fun save(item: HistoryRate): Completable {
        // Do not call it
        return Completable.never()
    }
}
