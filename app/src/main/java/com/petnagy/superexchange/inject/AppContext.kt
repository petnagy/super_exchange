package com.petnagy.superexchange.inject

import javax.inject.Qualifier

/***
 * AppContext annotation for Application Context in Dagger.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppContext