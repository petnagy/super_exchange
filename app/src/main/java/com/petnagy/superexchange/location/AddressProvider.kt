package com.petnagy.superexchange.location

import android.location.Location
import com.patloew.rxlocation.RxLocation
import io.reactivex.Single

class AddressProvider(private val rxLocation: RxLocation) {

    fun getCountryCode(location: Location): Single<String> {
        return rxLocation.geocoding().fromLocation(location).toSingle().map { address -> address.countryCode }
    }
}
