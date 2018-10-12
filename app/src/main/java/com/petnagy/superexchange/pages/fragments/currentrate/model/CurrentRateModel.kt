package com.petnagy.superexchange.pages.fragments.currentrate.model

import android.annotation.SuppressLint
import android.location.Address
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.permission.PermissionStatus
import com.petnagy.superexchange.repository.LatestRateCompositeRepository
import com.petnagy.superexchange.repository.LatestRateSpecification
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/***
 * Model class for Current Rate page.
 */
class CurrentRateModel(private val rxLocation: RxLocation, private val compositeRepository: LatestRateCompositeRepository) {

    private val locationRequest = LocationRequest()

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.numUpdates = 1
    }

    fun getBaseCurrency(fineLocationPermission: PermissionStatus): Observable<String> {
        return if (fineLocationPermission == PermissionStatus.PERMISSION_GRANTED) {
            rxLocation.settings().checkAndHandleResolution(locationRequest)
                    .flatMapObservable(this::getAddressObservable)
                    .map { address -> address.countryCode }
        } else {
            Observable.never()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getAddressObservable(success: Boolean): Observable<Address>? {
        return if (success) {
            rxLocation.location().updates(locationRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(this::getAddressFromLocation)
        } else {
            Observable.error(RuntimeException("Something wrong"))
        }
    }

    private fun getAddressFromLocation(location: Location): Observable<Address>? {
        return rxLocation.geocoding().fromLocation(location).toObservable()
                .doOnNext { address -> Timber.d(address.toString()) }
                .subscribeOn(Schedulers.io())
    }

    fun queryCurrentRate(): Single<LatestRate> {
        val symbols = Currency.values().joinToString(separator = ",")
        return compositeRepository.load(LatestRateSpecification(symbols, "EUR", "2018-10-12")).toSingle()
    }

}