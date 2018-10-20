package com.petnagy.superexchange.location

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import io.reactivex.Single

class PlayServiceChecker(private val context: Context) {

    fun checkPlayService(): Single<Boolean> {
        return Single.just(isGooglePlayServicesAvailable(context))
    }

    private fun isGooglePlayServicesAvailable(context: Context): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        return resultCode == ConnectionResult.SUCCESS
    }
}