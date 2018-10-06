package com.petnagy.superexchange.inject.components

import com.petnagy.superexchange.SuperExchange
import com.petnagy.superexchange.inject.modules.DaggerActivityModule
import com.petnagy.superexchange.inject.modules.DaggerApplicationModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/***
 * Dagger component for Application.
 */
@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class), (DaggerApplicationModule::class), (DaggerActivityModule::class)])
interface ApplicationComponent: AndroidInjector<SuperExchange> {

    @Component.Builder
    abstract class Builder: AndroidInjector.Builder<SuperExchange>()

}