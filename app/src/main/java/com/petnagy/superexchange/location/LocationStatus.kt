package com.petnagy.superexchange.location

/***
 * Latest rate status to show error dialog.
 */
enum class LocationStatus {

    STATUS_UNKNOWN,
    STATUS_OK,
    PERMISSION_NEED,
    PERMISSION_DENIED,
    PLAY_SERVICE_ERROR,
    SETTING_ERROR,
    LOCATION_ERROR,
    NETWORK_ERROR,
    NOT_VALID_COUNTRY_CODE
}
