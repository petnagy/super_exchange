package com.petnagy.superexchange.redux.action

import android.location.Location
import com.petnagy.koredux.Action
import com.petnagy.superexchange.permission.PermissionStatus

class StartLocationSearchAction(val permissionStatus: PermissionStatus): Action
class CheckPlayServiceAction: Action
class LocationSearchErrorAction: Action
class NoPlayServiceAction: Action
class CanAskPermissionAction: Action
class PermissionDeniedAction: Action
class CheckLocationSettingsAction: Action
class LocationSettingsErrorAction: Action
class RequestLocationAction: Action
class GetLocationAction(val location: Location): Action