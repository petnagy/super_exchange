package com.petnagy.superexchange.inject

import javax.inject.Scope

/***
 * PErActivity annotation for Activity level scope in Dagger.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity
