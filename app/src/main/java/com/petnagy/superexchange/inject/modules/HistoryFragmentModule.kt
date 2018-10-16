package com.petnagy.superexchange.inject.modules

import com.petnagy.koredux.Store
import com.petnagy.superexchange.convert.RateConverter
import com.petnagy.superexchange.pages.fragments.history.viewmodel.HistoryViewModelFactory
import com.petnagy.superexchange.redux.state.AppState
import dagger.Module
import dagger.Provides

@Module
class HistoryFragmentModule {

    @Provides
    internal fun provideRateConverter() = RateConverter()

    @Provides
    internal fun provideHistoryViewModelFactory(store: Store<AppState>, rateConverter: RateConverter): HistoryViewModelFactory = HistoryViewModelFactory(store, rateConverter)

}