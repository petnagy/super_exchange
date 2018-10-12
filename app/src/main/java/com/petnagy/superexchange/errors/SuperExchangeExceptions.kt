package com.petnagy.superexchange.errors

import java.lang.RuntimeException

class PermissionNotGrantedException: RuntimeException()

class PermissionDeniedException: RuntimeException()

class NoPlayServiceException: RuntimeException()

class LocationSettingsException: RuntimeException()
