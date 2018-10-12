package com.petnagy.superexchange.inject.modules

import android.content.Context
import com.patloew.rxlocation.RxLocation
import com.petnagy.superexchange.inject.AppContext
import com.petnagy.superexchange.location.AddressProvider
import com.petnagy.superexchange.location.LocationProvider
import com.petnagy.superexchange.location.LocationSettingChecker
import com.petnagy.superexchange.location.PlayServiceChecker
import com.petnagy.superexchange.pages.fragments.currentrate.model.CurrentRateModel
import com.petnagy.superexchange.pages.fragments.currentrate.viewmodel.CurrentRateViewModelFactory
import com.petnagy.superexchange.permission.PermissionManager
import com.petnagy.superexchange.repository.LatestRateCompositeRepository
import dagger.Module
import dagger.Provides

/***
 * Current Rate related injections.
 */
@Module
class CurrentRateFragmentModule {

    @Provides
    internal fun provideRxLocation(@AppContext context: Context) = RxLocation(context)

    @Provides
    internal fun providePlayServiceChecker(@AppContext context: Context) = PlayServiceChecker(context)

    @Provides
    internal fun provideSettingsChecker(rxLocation: RxLocation) = LocationSettingChecker(rxLocation)

    @Provides
    internal fun provideLocationProvider(rxLocation: RxLocation) = LocationProvider(rxLocation)

    @Provides
    internal fun provideAddressProvider(rxLocation: RxLocation) = AddressProvider(rxLocation)

    @Provides
    internal fun provideCurrentRateModel(playServiceChecker: PlayServiceChecker, locationSettingChecker: LocationSettingChecker,
                                         locationProvider: LocationProvider, addressProvider: AddressProvider,
                                         compositeRepository: LatestRateCompositeRepository) =
            CurrentRateModel(playServiceChecker, locationSettingChecker, locationProvider, addressProvider, compositeRepository)

    @Provides
    internal fun provideViewModelFactory(currentRateModel: CurrentRateModel): CurrentRateViewModelFactory = CurrentRateViewModelFactory(currentRateModel)

    @Provides
    internal fun providePermissionManager() = PermissionManager()
}