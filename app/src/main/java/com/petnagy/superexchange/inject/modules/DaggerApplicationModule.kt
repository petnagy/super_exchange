package com.petnagy.superexchange.inject.modules

import android.content.Context
import com.petnagy.superexchange.SuperExchange
import com.petnagy.superexchange.inject.AppContext
import dagger.Module
import dagger.Provides

/***
 * Application level injection.
 */
@Module
class DaggerApplicationModule {

    @Provides
    @AppContext
    internal fun provideAppContext(application: SuperExchange): Context = application.applicationContext

}