package com.petnagy.superexchange.redux.middleware

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Middleware
import com.petnagy.koredux.Store
import com.petnagy.superexchange.data.Country
import com.petnagy.superexchange.location.AddressProvider
import com.petnagy.superexchange.location.LocationProvider
import com.petnagy.superexchange.location.LocationSettingChecker
import com.petnagy.superexchange.location.LocationStatus
import com.petnagy.superexchange.location.PlayServiceChecker
import com.petnagy.superexchange.permission.PermissionStatus
import com.petnagy.superexchange.redux.action.CheckLocationSettingsAction
import com.petnagy.superexchange.redux.action.CheckPlayServiceAction
import com.petnagy.superexchange.redux.action.GetLocationAction
import com.petnagy.superexchange.redux.action.LatestRateErrorAction
import com.petnagy.superexchange.redux.action.RequestLocationAction
import com.petnagy.superexchange.redux.action.SetBaseCurrencyAction
import com.petnagy.superexchange.redux.action.StartLocationSearchAction
import com.petnagy.superexchange.redux.state.AppState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LocationMiddleware(
    private val playServiceChecker: PlayServiceChecker,
    private val locationSettingChecker: LocationSettingChecker,
    private val locationProvider: LocationProvider,
    private val addressProvider: AddressProvider
) : Middleware<AppState> {

    companion object {
        private const val INTERVAL_VALUE: Long = 5000
        private const val NUMBER_OF_UPDATES = 1
    }

    private val locationRequest = LocationRequest()

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = INTERVAL_VALUE
        locationRequest.numUpdates = NUMBER_OF_UPDATES
    }

    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
        when (action) {
            is StartLocationSearchAction -> checkPermission(store, action.permissionStatus)
            is CheckPlayServiceAction -> checkPlayService(store)
            is CheckLocationSettingsAction -> checkLocationSettings(store)
            is RequestLocationAction -> requestLocation(store)
            is GetLocationAction -> requestAddress(store, action.location)
        }
        next.dispatch(action)
    }

    private fun checkPermission(store: Store<AppState>, permissionStatus: PermissionStatus) {
        when (permissionStatus) {
            PermissionStatus.PERMISSION_GRANTED -> store.dispatch(CheckPlayServiceAction())
            PermissionStatus.CAN_ASK_PERMISSION -> store.dispatch(LatestRateErrorAction(LocationStatus.PERMISSION_NEED))
            PermissionStatus.PERMISSION_DENIED -> store.dispatch(LatestRateErrorAction(LocationStatus.PERMISSION_DENIED))
        }
    }

    @SuppressLint("CheckResult")
    private fun checkPlayService(store: Store<AppState>) {
        playServiceChecker.checkPlayService()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { success -> handlePlayCheck(store, success) },
                        { error -> handleError(store, error) }
                )
    }

    private fun handleError(store: Store<AppState>, error: Throwable) {
        store.dispatch(LatestRateErrorAction(LocationStatus.LOCATION_ERROR))
        Timber.e(error, "Something went wrong!")
    }

    private fun handlePlayCheck(store: Store<AppState>, success: Boolean) {
        if (success) {
            store.dispatch(CheckLocationSettingsAction())
        } else {
            store.dispatch(LatestRateErrorAction(LocationStatus.PLAY_SERVICE_ERROR))
        }
    }

    @SuppressLint("CheckResult")
    private fun checkLocationSettings(store: Store<AppState>) {
        locationSettingChecker.checkLocationSettings(locationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { success -> handleLocationSettings(store, success) },
                        { error -> handleError(store, error) }
                )
    }

    private fun handleLocationSettings(store: Store<AppState>, success: Boolean) {
        if (success) {
            store.dispatch(RequestLocationAction())
        } else {
            store.dispatch(LatestRateErrorAction(LocationStatus.SETTING_ERROR))
        }
    }

    @SuppressLint("CheckResult")
    private fun requestLocation(store: Store<AppState>) {
        locationProvider.getLocation(locationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { location -> store.dispatch(GetLocationAction(location)) },
                        { error -> handleError(store, error) }
                )
    }

    @SuppressLint("CheckResult")
    private fun requestAddress(store: Store<AppState>, location: Location) {
        addressProvider.getCountryCode(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { countryCode -> handleCountryCode(store, countryCode) },
                        { error -> handleError(store, error) }
                )
    }

    private fun handleCountryCode(store: Store<AppState>, countryCode: String) {
        if (Country.checkCountryCodeSupported(countryCode)) {
            store.dispatch(SetBaseCurrencyAction(Country.valueOf(countryCode).currency))
        } else {
            store.dispatch(LatestRateErrorAction(LocationStatus.NOT_VALID_COUNTRY_CODE))
        }
    }
}
