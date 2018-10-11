package com.petnagy.superexchange.network

import com.petnagy.superexchange.data.LatestRate
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerIoEndpoint {

    @GET("/latest")
    fun getCurrentRate(@Query("symbols") symbols: String, @Query("access_key") apiKey: String): Single<LatestRate>

}