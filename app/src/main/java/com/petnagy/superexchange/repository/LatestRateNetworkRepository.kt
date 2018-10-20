package com.petnagy.superexchange.repository

import com.petnagy.superexchange.BuildConfig
import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.network.FixerIoEndpoint
import io.reactivex.Completable
import io.reactivex.Maybe

/***
 * Network repository to get LatestRate data.
 */
class LatestRateNetworkRepository(private val endpoint: FixerIoEndpoint) : Repository<LatestRate> {

    override fun load(specification: Specification): Maybe<LatestRate> {
        return if (specification !is LatestRateSpecification) {
            Maybe.error(IllegalArgumentException("Wrong specification"))
        } else {
            //Put your API key in your gradle.properties file in your home directory
            endpoint.getCurrentRate(specification.symbols, BuildConfig.ApiKey).toMaybe()
        }
    }

    override fun save(item: LatestRate): Completable {
        //It will not used
        return Completable.never()
    }

}