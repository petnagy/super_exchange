package com.petnagy.superexchange.pages.fragments.currentrate.model

import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.errors.LocationSettingsException
import com.petnagy.superexchange.errors.NoPlayServiceException
import com.petnagy.superexchange.errors.PermissionDeniedException
import com.petnagy.superexchange.errors.PermissionNotGrantedException
import com.petnagy.superexchange.location.AddressProvider
import com.petnagy.superexchange.location.LocationProvider
import com.petnagy.superexchange.location.LocationSettingChecker
import com.petnagy.superexchange.location.PlayServiceChecker
import com.petnagy.superexchange.permission.PermissionStatus
import com.petnagy.superexchange.repository.LatestRateCompositeRepository
import com.petnagy.superexchange.repository.LatestRateSpecification
import com.petnagy.superexchange.repository.Repository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/***
 * Model class for Current Rate page.
 */
class CurrentRateModel(private val playServiceChecker: PlayServiceChecker,
                       private val locationSettingChecker: LocationSettingChecker,
                       private val locationProvider: LocationProvider,
                       private val addressProvider: AddressProvider,
                       private val compositeRepository: Repository<LatestRate>) {

    private val locationRequest = LocationRequest()

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.numUpdates = 1
    }

    fun getBaseCurrency(fineLocationPermission: PermissionStatus): Observable<String> {
        return when(fineLocationPermission) {
            PermissionStatus.PERMISSION_GRANTED -> playServiceChecker.checkPlayService().flatMap(this::checkSettings).flatMapObservable(this::getLocation).flatMapSingle(this::getCountryCode)
            PermissionStatus.CAN_ASK_PERMISSION -> Observable.error(PermissionDeniedException())
            PermissionStatus.PERMISSION_DENIED -> Observable.error(PermissionNotGrantedException())
        }
    }

    fun queryCurrentRate(baseCurrency: String): Single<LatestRate> {
        val symbols = Currency.values().joinToString(separator = ",")
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        return compositeRepository.load(LatestRateSpecification(symbols, baseCurrency, date)).toSingle()
    }

    private fun checkSettings(playServiceAvailable: Boolean): Single<Boolean> {
        return if (playServiceAvailable) {
            locationSettingChecker.checkLocationSettings(locationRequest)
        } else {
            Single.error(NoPlayServiceException())
        }
    }

    private fun getLocation(success: Boolean): Observable<Location> {
        return if (success) {
            locationProvider.getLocation(locationRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { location -> Timber.d(location.toString()) }
        } else {
            Observable.error(LocationSettingsException())
        }
    }

    private fun getCountryCode(location: Location): Single<String> {
        return addressProvider.getCountryCode(location)
                .doOnSuccess { address -> Timber.d(address) }
                .subscribeOn(Schedulers.io())

    }

}