package com.petnagy.superexchange.inject.modules

import com.petnagy.superexchange.inject.PerActivity
import com.petnagy.superexchange.pages.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/***
 * Activity level injections
 */
@Module
interface DaggerActivityModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    fun contributeMainActivityInjector(): MainActivity
}