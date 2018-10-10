package com.petnagy.superexchange.pages.fragments.currentrate.model

import com.petnagy.superexchange.permission.PermissionStatus
import io.reactivex.Single

/***
 * Model class for Current Rate page.
 */
class CurrentRateModel {

    fun getBaseCurrency(fineLocationPermission: PermissionStatus): Single<String> {
        return if (fineLocationPermission == PermissionStatus.PERMISSION_GRANTED) {
            Single.just("HUF")
        } else {
            Single.never()
        }
    }

}