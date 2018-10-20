package com.petnagy.superexchange.extensions

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData

/***
 * Extensions functions.
 */
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

fun <T : Any?> MediatorLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }
