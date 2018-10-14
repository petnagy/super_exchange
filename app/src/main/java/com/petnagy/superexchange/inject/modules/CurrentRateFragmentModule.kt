package com.petnagy.superexchange.inject.modules

import com.petnagy.superexchange.convert.RateConverter
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
    internal fun provideCurrentRateModel(playServiceChecker: PlayServiceChecker, locationSettingChecker: LocationSettingChecker,
                                         locationProvider: LocationProvider, addressProvider: AddressProvider,
                                         compositeRepository: LatestRateCompositeRepository) =
            CurrentRateModel(playServiceChecker, locationSettingChecker, locationProvider, addressProvider, compositeRepository)

    @Provides
    internal fun provideRateConverter() = RateConverter()

    @Provides
    internal fun provideViewModelFactory(currentRateModel: CurrentRateModel, rateConverter: RateConverter): CurrentRateViewModelFactory = CurrentRateViewModelFactory(currentRateModel, rateConverter)

    @Provides
    internal fun providePermissionManager() = PermissionManager()
}