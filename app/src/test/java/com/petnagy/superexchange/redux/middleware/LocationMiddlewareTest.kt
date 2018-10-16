package com.petnagy.superexchange.redux.middleware

import android.location.Location
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Store
import com.petnagy.superexchange.RxImmediateSchedulerRule
import com.petnagy.superexchange.data.Country
import com.petnagy.superexchange.location.*
import com.petnagy.superexchange.permission.PermissionStatus
import com.petnagy.superexchange.redux.action.*
import com.petnagy.superexchange.redux.state.AppState
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocationMiddlewareTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    private lateinit var underTest: LocationMiddleware

    private lateinit var mockedPlayChecker: PlayServiceChecker

    private lateinit var mockedSettingChecker: LocationSettingChecker

    private lateinit var mockedLocationProvider: LocationProvider

    private lateinit var mockedAddressProvider: AddressProvider

    private lateinit var mockedStore: Store<AppState>

    private lateinit var mockedDispatchFunction: DispatchFunction

    @Before
    fun setUp() {
        mockedPlayChecker = mock()
        mockedSettingChecker = mock()
        mockedLocationProvider = mock()
        mockedAddressProvider = mock()
        mockedStore = mock()
        mockedDispatchFunction = mock()
        underTest = LocationMiddleware(mockedPlayChecker, mockedSettingChecker, mockedLocationProvider, mockedAddressProvider)
    }

    @Test
    fun testStartLocationSearchAction_And_PermissionDenied() {
        //GIVEN

        //WHEN
        underTest.invoke(mockedStore, StartLocationSearchAction(PermissionStatus.PERMISSION_DENIED), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(LatestRateErrorAction(LatestRateStatus.PERMISSION_DENIED))
    }

    @Test
    fun testStartLocationSearchAction_And_PermissionAskPermission() {
        //GIVEN

        //WHEN
        underTest.invoke(mockedStore, StartLocationSearchAction(PermissionStatus.CAN_ASK_PERMISSION), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(LatestRateErrorAction(LatestRateStatus.PERMISSION_NEED))
    }

    @Test
    fun testStartLocationSearchAction_And_PermissionCheckSuccess() {
        //GIVEN

        //WHEN
        underTest.invoke(mockedStore, StartLocationSearchAction(PermissionStatus.PERMISSION_GRANTED), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(CheckPlayServiceAction())
    }

    @Test
    fun testCheckPlayServiceAction_And_Failed() {
        //GIVEN

        //WHEN
        Mockito.`when`(mockedPlayChecker.checkPlayService()).thenReturn(Single.error(RuntimeException()))
        underTest.invoke(mockedStore, CheckPlayServiceAction(), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(LatestRateErrorAction(LatestRateStatus.LOCATION_ERROR))
    }

    @Test
    fun testCheckPlayServiceAction_And_NoPlayService() {
        //GIVEN

        //WHEN
        Mockito.`when`(mockedPlayChecker.checkPlayService()).thenReturn(Single.just(false))
        underTest.invoke(mockedStore, CheckPlayServiceAction(), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(LatestRateErrorAction(LatestRateStatus.PLAY_SERVICE_ERROR))
    }

    @Test
    fun testCheckPlayServiceAction_And_Success() {
        //GIVEN

        //WHEN
        Mockito.`when`(mockedPlayChecker.checkPlayService()).thenReturn(Single.just(true))
        underTest.invoke(mockedStore, CheckPlayServiceAction(), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(CheckLocationSettingsAction())
    }

    @Test
    fun testCheckLocationSettingsAction_And_Failed() {
        //GIVEN

        //WHEN
        Mockito.`when`(mockedSettingChecker.checkLocationSettings(any())).thenReturn(Single.error(RuntimeException()))
        underTest.invoke(mockedStore, CheckLocationSettingsAction(), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(LatestRateErrorAction(LatestRateStatus.LOCATION_ERROR))
    }

    @Test
    fun testCheckLocationSettingsAction_And_SettingsError() {
        //GIVEN

        //WHEN
        Mockito.`when`(mockedSettingChecker.checkLocationSettings(any())).thenReturn(Single.just(false))
        underTest.invoke(mockedStore, CheckLocationSettingsAction(), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(LatestRateErrorAction(LatestRateStatus.SETTING_ERROR))
    }

    @Test
    fun testCheckLocationSettingsAction_And_Success() {
        //GIVEN

        //WHEN
        Mockito.`when`(mockedSettingChecker.checkLocationSettings(any())).thenReturn(Single.just(true))
        underTest.invoke(mockedStore, CheckLocationSettingsAction(), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(RequestLocationAction())
    }

    @Test
    fun testRequestLocationAction_And_Failed() {
        //GIVEN

        //WHEN
        Mockito.`when`(mockedLocationProvider.getLocation(any())).thenReturn(Observable.error(RuntimeException()))
        underTest.invoke(mockedStore, RequestLocationAction(), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(LatestRateErrorAction(LatestRateStatus.LOCATION_ERROR))
    }

    @Test
    fun testRequestLocationAction_And_Success() {
        //GIVEN

        //WHEN
        val mockedLocation: Location = mock()
        Mockito.`when`(mockedLocationProvider.getLocation(any())).thenReturn(Observable.just(mockedLocation))
        underTest.invoke(mockedStore, RequestLocationAction(), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(GetLocationAction(mockedLocation))
    }

    @Test
    fun testGetLocationAction_And_Failed() {
        //GIVEN

        //WHEN
        val mockedLocation: Location = mock()
        Mockito.`when`(mockedAddressProvider.getCountryCode(any())).thenReturn(Single.error(RuntimeException()))
        underTest.invoke(mockedStore, GetLocationAction(mockedLocation), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(LatestRateErrorAction(LatestRateStatus.LOCATION_ERROR))
    }

    @Test
    fun testGetLocationAction_And_Success_But_Not_Valid() {
        //GIVEN

        //WHEN
        val countryCode = "countryCode"
        val mockedLocation: Location = mock()
        Mockito.`when`(mockedAddressProvider.getCountryCode(any())).thenReturn(Single.just(countryCode))
        underTest.invoke(mockedStore, GetLocationAction(mockedLocation), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(LatestRateErrorAction(LatestRateStatus.NOT_VALID_COUNTRY_CODE))
    }

    @Test
    fun testGetLocationAction_And_Success() {
        //GIVEN

        //WHEN
        val countryCode = "HU"
        val mockedLocation: Location = mock()
        Mockito.`when`(mockedAddressProvider.getCountryCode(any())).thenReturn(Single.just(countryCode))
        underTest.invoke(mockedStore, GetLocationAction(mockedLocation), mockedDispatchFunction)

        //THEN
        Mockito.verify(mockedStore).dispatch(SetBaseCurrencyAction(Country.HU.currency))
    }
}