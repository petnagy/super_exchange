package com.petnagy.superexchange.inject.modules

import android.content.Context
import com.patloew.rxlocation.RxLocation
import com.petnagy.superexchange.inject.AppContext
import com.petnagy.superexchange.network.FixerIoEndpoint
import com.petnagy.superexchange.pages.fragments.currentrate.model.CurrentRateModel
import com.petnagy.superexchange.pages.fragments.currentrate.viewmodel.CurrentRateViewModelFactory
import com.petnagy.superexchange.permission.PermissionManager
import com.petnagy.superexchange.repository.LatestRateCompositeRepository
import com.petnagy.superexchange.repository.LatestRateNetworkRepository
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
    internal fun provideCurrentRateModel(rxLocation: RxLocation, compositeRepository: LatestRateCompositeRepository) = CurrentRateModel(rxLocation, compositeRepository)

    @Provides
    internal fun provideViewModelFactory(currentRateModel: CurrentRateModel): CurrentRateViewModelFactory = CurrentRateViewModelFactory(currentRateModel)

    @Provides
    internal fun providePermissionManager() = PermissionManager()
}