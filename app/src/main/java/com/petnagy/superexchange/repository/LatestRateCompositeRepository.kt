package com.petnagy.superexchange.repository

import com.petnagy.superexchange.data.LatestRate
import io.reactivex.Completable
import io.reactivex.Maybe
import timber.log.Timber

/***
 * Composite repository, it is using network and room repository.
 */
class LatestRateCompositeRepository(private val networkRepo: LatestRateNetworkRepository, private val roomRepo: LatestRateRoomRepository) : Repository<LatestRate> {

    override fun load(specification: Specification): Maybe<LatestRate> {
        return if (specification !is LatestRateSpecification) {
            Maybe.error(IllegalArgumentException("Wrong specification"))
        } else {
            roomRepo.load(specification).doOnSuccess { Timber.d("Load from cache") }
                    .switchIfEmpty(networkRepo.load(specification).doAfterSuccess { latestRate -> roomRepo.save(latestRate).subscribe() })
        }
    }

    override fun save(item: LatestRate): Completable {
        // Do not call it
        return Completable.never()
    }
}