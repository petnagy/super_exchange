package com.petnagy.superexchange.data

import java.math.BigDecimal

data class LatestRate(val success: Boolean, val timestamp: Long, val base: String, val date: String, val rates: Map<String, BigDecimal>)