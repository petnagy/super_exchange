package com.petnagy.superexchange.data

import android.arch.persistence.room.Entity
import java.math.BigDecimal

/***
 * LatestRate is actual rate(s) object.
 */
@Entity(tableName = "latest_rate", primaryKeys = ["base", "date"])
data class LatestRate(val success: Boolean, val timestamp: Long, val base: String, val date: String, val rates: Map<String, BigDecimal>)