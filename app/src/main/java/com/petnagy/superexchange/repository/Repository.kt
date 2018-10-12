package com.petnagy.superexchange.repository

import io.reactivex.Completable
import io.reactivex.Maybe

/***
 * General Repository interface.
 */
interface Repository<T> {

    fun load(specification: Specification): Maybe<T>

    fun save(item: T): Completable
}