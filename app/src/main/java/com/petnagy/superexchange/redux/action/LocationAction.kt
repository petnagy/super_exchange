package com.petnagy.superexchange.redux.action

import android.location.Location
import com.petnagy.koredux.Action
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.location.LatestRateStatus
import com.petnagy.superexchange.permission.PermissionStatus

class StartLocationSearchAction(val permissionStatus: PermissionStatus) : Action
class LatestRateErrorAction(val status: LatestRateStatus): Action
class CheckPlayServiceAction: Action
class CheckLocationSettingsAction : Action
class RequestLocationAction : Action
class GetLocationAction(val location: Location) : Action
class SetBaseCurrencyAction(val baseCurrency: Currency) : Action