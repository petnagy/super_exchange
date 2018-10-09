package com.petnagy.superexchange.pages.fragments.currentrate.model

import io.reactivex.Single

/***
 * Model class for Current Rate page.
 */
class CurrentRateModel {

    fun getBaseCurrency(): Single<String> {
        return Single.just("HUF")
    }

}