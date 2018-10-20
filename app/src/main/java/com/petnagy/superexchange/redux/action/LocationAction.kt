package com.petnagy.superexchange.redux.action

import android.location.Location
import com.petnagy.koredux.Action
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.location.LocationStatus
import com.petnagy.superexchange.permission.PermissionStatus

class StartLocationSearchAction(val permissionStatus: PermissionStatus) : LocationAction()
class LatestRateErrorAction(val status: LocationStatus) : LocationAction()
class CheckPlayServiceAction : LocationAction()
class CheckLocationSettingsAction : LocationAction()
class RequestLocationAction : LocationAction()
class GetLocationAction(val location: Location) : LocationAction()
class SetBaseCurrencyAction(val baseCurrency: Currency) : LocationAction()

open class LocationAction : Action {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LocationAction) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
