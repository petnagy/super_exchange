package com.petnagy.superexchange.inject.components

import com.petnagy.superexchange.SuperExchange
import com.petnagy.superexchange.inject.modules.DaggerActivityModule
import com.petnagy.superexchange.inject.modules.DaggerApplicationModule
import com.petnagy.superexchange.inject.modules.DaggerFragmentModule
import com.petnagy.superexchange.network.FixerIoEndpoint
import com.petnagy.superexchange.repository.HistoryRateCompositeRepository
import com.petnagy.superexchange.repository.LatestRateCompositeRepository
import com.petnagy.superexchange.room.AppDatabase
import com.petnagy.superexchange.room.HistoryRateDao
import com.petnagy.superexchange.room.LatestRateDao
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/***
 * Dagger component for Application.
 */
@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class),
    (DaggerApplicationModule::class),
    (DaggerActivityModule::class),
    (DaggerFragmentModule::class)])
interface ApplicationComponent : AndroidInjector<SuperExchange> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<SuperExchange>()

    fun exposeFixerIoEndpoint(): FixerIoEndpoint

    fun exposeLAtestRateCompositeRepository(): LatestRateCompositeRepository

    fun exposeHistoryRateCompositeRepo(): HistoryRateCompositeRepository

    fun exposeLatestRateDao(): LatestRateDao

    fun exposeHistoryRateDao(): HistoryRateDao

    fun exposeAppDatabase(): AppDatabase
}
