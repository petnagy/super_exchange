package com.petnagy.superexchange.inject.modules

import com.petnagy.superexchange.pages.fragments.currentrate.CurrentRateFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/***
 * Fragment level injection.
 */
@Module
interface DaggerFragmentModule {

    @ContributesAndroidInjector(modules = [(CurrentRateFragmentModule::class)])
    fun contributeCurrentRateFragment(): CurrentRateFragment

}