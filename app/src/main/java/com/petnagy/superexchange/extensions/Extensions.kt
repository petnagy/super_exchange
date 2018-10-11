package com.petnagy.superexchange.extensions

import android.arch.lifecycle.MutableLiveData

/***
 * Extensions functions.
 */
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }
