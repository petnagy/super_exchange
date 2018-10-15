package com.petnagy.superexchange.inject.modules

import com.petnagy.koredux.Store
import com.petnagy.superexchange.convert.RateConverter
import com.petnagy.superexchange.pages.fragments.currentrate.viewmodel.CurrentRateViewModelFactory
import com.petnagy.superexchange.permission.PermissionManager
import com.petnagy.superexchange.redux.state.AppState
import dagger.Module
import dagger.Provides

/***
 * Current Rate related injections.
 */
@Module
class CurrentRateFragmentModule {

    @Provides
    internal fun provideRateConverter() = RateConverter()

    @Provides
    internal fun provideViewModelFactory(store: Store<AppState>, rateConverter: RateConverter): CurrentRateViewModelFactory = CurrentRateViewModelFactory(store, rateConverter)

    @Provides
    internal fun providePermissionManager() = PermissionManager()
}