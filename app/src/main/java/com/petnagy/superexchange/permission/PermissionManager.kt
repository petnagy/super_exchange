package com.petnagy.superexchange.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/***
 * Permission manager class to know Access fine permission is granted or not.
 */
class PermissionManager {

    fun getPermissionStatus(activity: Activity, permission: String): PermissionStatus {
        return when {
            PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, permission) -> PermissionStatus.PERMISSION_GRANTED
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> PermissionStatus.CAN_ASK_PERMISSION
            else -> PermissionStatus.PERMISSION_DENIED
        }
    }

}