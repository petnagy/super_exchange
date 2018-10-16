package com.petnagy.superexchange.inject.modules

import com.petnagy.superexchange.pages.fragments.currentrate.CurrentRateFragment
import com.petnagy.superexchange.pages.fragments.history.HistoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/***
 * Fragment level injection.
 */
@Module
abstract class DaggerFragmentModule {

    @ContributesAndroidInjector(modules = [(CurrentRateFragmentModule::class)])
    internal abstract fun contributeCurrentRateFragment(): CurrentRateFragment

    @ContributesAndroidInjector(modules = [(HistoryFragmentModule::class)])
    internal abstract fun contributeHistoryFragment(): HistoryFragment
}