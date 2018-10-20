package com.petnagy.superexchange.repository

import com.petnagy.superexchange.BuildConfig
import com.petnagy.superexchange.data.HistoryRate
import com.petnagy.superexchange.network.FixerIoEndpoint
import io.reactivex.Completable
import io.reactivex.Maybe

/***
 * History rate network repository get history rate from the endpoint.
 */
class HistoryRateNetworkRepository(private val endpoint: FixerIoEndpoint) : Repository<HistoryRate> {

    override fun load(specification: Specification): Maybe<HistoryRate> {
        return if (specification !is HistoryRateSpecification) {
            Maybe.error(IllegalArgumentException("Wrong specification"))
        } else {
            // Put your API key in your gradle.properties file in your home directory
            endpoint.getHistoryRate(specification.date, specification.symbols, BuildConfig.ApiKey).toMaybe()
        }
    }

    override fun save(item: HistoryRate): Completable {
        // It will not used
        return Completable.never()
    }
}
