package com.petnagy.superexchange.network

import com.petnagy.superexchange.data.HistoryRate
import com.petnagy.superexchange.data.LatestRate
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/***
 * Fixer IO related endpoint calls for retrofit.
 */
interface FixerIoEndpoint {

    @GET("/latest")
    fun getCurrentRate(@Query("symbols") symbols: String, @Query("access_key") apiKey: String): Single<LatestRate>

    @GET("/{date}")
    fun getHistoryRate(@Path("date") dateValue: String, @Query("symbols") symbols: String, @Query("access_key") apiKey: String): Single<HistoryRate>
}