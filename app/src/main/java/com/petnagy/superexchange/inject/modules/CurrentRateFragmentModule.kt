package com.petnagy.superexchange.inject.modules

import com.petnagy.superexchange.pages.fragments.currentrate.model.CurrentRateModel
import com.petnagy.superexchange.pages.fragments.currentrate.viewmodel.CurrentRateViewModelFactory
import com.petnagy.superexchange.permission.PermissionManager
import dagger.Module
import dagger.Provides

/***
 * Current Rate related injections.
 */
@Module
class CurrentRateFragmentModule {

    @Provides
    internal fun provideCurrentRateModel() = CurrentRateModel()

    @Provides
    internal fun provideViewModelFactory(currentRateModel: CurrentRateModel): CurrentRateViewModelFactory = CurrentRateViewModelFactory(currentRateModel)

    @Provides
    internal fun providePermissionManager() = PermissionManager()
}