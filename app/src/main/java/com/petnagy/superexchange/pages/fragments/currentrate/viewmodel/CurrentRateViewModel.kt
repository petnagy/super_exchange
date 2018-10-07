package com.petnagy.superexchange.pages.fragments.currentrate.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/***
 * Current Rate related ViewModel.
 */
class CurrentRateViewModel: ViewModel() {

    val loading = MutableLiveData<Boolean>()

}