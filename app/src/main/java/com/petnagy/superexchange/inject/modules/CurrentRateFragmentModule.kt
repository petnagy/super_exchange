package com.petnagy.superexchange.inject.modules

import com.petnagy.superexchange.pages.fragments.currentrate.model.CurrentRateModel
import com.petnagy.superexchange.pages.fragments.currentrate.viewmodel.CurrentRateViewModelFactory
import dagger.Module
import dagger.Provides

/***
 * Current Rate related injections.
 */
@Module
class CurrentRateFragmentModule {

    @Provides
    fun provideCurrentRateModel() = CurrentRateModel()


    @Provides
    internal fun provideViewModelFactory(currentRateModel: CurrentRateModel): CurrentRateViewModelFactory = CurrentRateViewModelFactory(currentRateModel)

}