package com.petnagy.superexchange.location

import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import io.reactivex.Single

class LocationSettingChecker(private val rxLocation: RxLocation) {

    fun checkLocationSettings(locationRequest: LocationRequest): Single<Boolean> {
        return rxLocation.settings().checkAndHandleResolution(locationRequest)
    }
}
