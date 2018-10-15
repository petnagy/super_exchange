package com.petnagy.superexchange.location

/***
 * Location status to show error dialog.
 */
enum class LocationStatus {

    STATUS_OK,
    PERMISSION_NEED,
    PERMISSION_DENIED,
    PLAY_SERVICE_ERROR,
    SETTING_ERROR,
    LOCATION_ERROR,
    NETWORK_ERROR
}