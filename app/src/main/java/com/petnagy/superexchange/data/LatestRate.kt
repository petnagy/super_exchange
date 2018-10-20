package com.petnagy.superexchange.data

import android.arch.persistence.room.Entity
import java.math.BigDecimal

/***
 * LatestRate is actual rate(s) object.
 */
@Entity(tableName = "latest_rate", primaryKeys = ["base", "date"])
data class LatestRate(val success: Boolean, val timestamp: Long, val base: String, val date: String, val rates: Map<String, BigDecimal>)

/***
 * Historical rate's object.
 */
@Entity(tableName = "history_rate", primaryKeys = ["base", "date"])
data class HistoryRate(val success: Boolean, val historical: Boolean, val date: String, val timestamp: Long, val base: String, val rates: Map<String, BigDecimal>)
