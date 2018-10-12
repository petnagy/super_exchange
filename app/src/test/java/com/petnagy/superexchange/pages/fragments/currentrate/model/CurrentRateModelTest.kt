package com.petnagy.superexchange.pages.fragments.currentrate.model

import android.location.Location
import com.google.android.gms.location.LocationRequest
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
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class CurrentRateModelTest {

    private lateinit var underTest: CurrentRateModel

    private lateinit var mockedPlayServiceChecker: PlayServiceChecker

    private lateinit var mockedLocSettingChecker: LocationSettingChecker

    private lateinit var mockedLocationProvider: LocationProvider

    private lateinit var mockedAddressProvider: AddressProvider

    private lateinit var mockedRepository: LatestRateCompositeRepository

    inline fun <reified T: Any> mock() = Mockito.mock(T::class.java)

    companion object {
        @ClassRule @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Before
    fun setUp() {
        mockedPlayServiceChecker = mock()
        mockedLocSettingChecker = mock()
        mockedLocationProvider = mock()
        mockedAddressProvider = mock()
        mockedRepository = mock()
        underTest = CurrentRateModel(mockedPlayServiceChecker, mockedLocSettingChecker, mockedLocationProvider, mockedAddressProvider, mockedRepository)
    }

    @Test
    fun testGetBaseCurrencyIfPCanAskPermission() {
        //GIVEN
        val subscriber = TestObserver<String>()
        val observable: Observable<String> = underTest.getBaseCurrency(PermissionStatus.CAN_ASK_PERMISSION)

        //WHEN
        observable.subscribe(subscriber)

        //THEN
        subscriber.assertError(PermissionDeniedException::class.java)
        subscriber.assertNotComplete()
    }

    @Test
    fun testGetBaseCurrencyIfPermissionIsDenied() {
        //GIVEN
        val subscriber = TestObserver<String>()
        val observable: Observable<String> = underTest.getBaseCurrency(PermissionStatus.PERMISSION_DENIED)

        //WHEN
        observable.subscribe(subscriber)

        //THEN
        subscriber.assertError(PermissionNotGrantedException::class.java)
        subscriber.assertNotComplete()
    }

    @Test
    fun testGetBaseCurrencyIfPlayServiceNotAvailable() {
        //GIVEN
        Mockito.`when`(mockedPlayServiceChecker.checkPlayService()).thenReturn(Single.just(false))
        val subscriber = TestObserver<String>()
        val observable: Observable<String> = underTest.getBaseCurrency(PermissionStatus.PERMISSION_GRANTED)

        //WHEN
        observable.subscribe(subscriber)

        //THEN
        subscriber.assertError(NoPlayServiceException::class.java)
        subscriber.assertNotComplete()
    }

    @Test
    fun testGetBaseCurrencyIfLocationSettingsWrong() {
        //GIVEN
        val request = LocationRequest()
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        request.interval = 5000
        request.numUpdates = 1
        Mockito.`when`(mockedPlayServiceChecker.checkPlayService()).thenReturn(Single.just(true))
        Mockito.`when`(mockedLocSettingChecker.checkLocationSettings(request)).thenReturn(Single.just(false))
        val subscriber = TestObserver<String>()
        val observable: Observable<String> = underTest.getBaseCurrency(PermissionStatus.PERMISSION_GRANTED)

        //WHEN
        observable.subscribe(subscriber)

        //THEN
        subscriber.assertError(LocationSettingsException::class.java)
        subscriber.assertNotComplete()
    }

    @Test
    fun testGetBaseCurrencyUseCountryCodeFromAddress() {
        //GIVEN
        val request = LocationRequest()
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        request.interval = 5000
        request.numUpdates = 1
        Mockito.`when`(mockedPlayServiceChecker.checkPlayService()).thenReturn(Single.just(true))
        Mockito.`when`(mockedLocSettingChecker.checkLocationSettings(request)).thenReturn(Single.just(true))
        val location: Location = mock()
        Mockito.`when`(mockedLocationProvider.getLocation(request)).thenReturn(Observable.just(location))
        val countryCode = "CODE"
        Mockito.`when`(mockedAddressProvider.getCountryCode(location)).thenReturn(Single.just(countryCode))

        val subscriber = TestObserver<String>()
        val observable: Observable<String> = underTest.getBaseCurrency(PermissionStatus.PERMISSION_GRANTED)

        //WHEN
        observable.subscribe(subscriber)

        //THEN
        subscriber.assertComplete()
        subscriber.assertValue(countryCode)
    }
}