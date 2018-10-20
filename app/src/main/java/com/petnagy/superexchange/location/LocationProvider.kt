package com.petnagy.superexchange.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import io.reactivex.Observable

class LocationProvider(private val rxLocation: RxLocation) {

    @SuppressLint("MissingPermission")
    fun getLocation(locationRequest: LocationRequest): Observable<Location> {
        return rxLocation.location().updates(locationRequest)
    }
}
