package com.petnagy.superexchange.room

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal

/***
 * TypeConverter for Map<String, BigDecimal> objects.
 */
class TypeConverter {

    @TypeConverter
    fun fromMap(dataMap: Map<String, BigDecimal>): String {
        val gson = Gson()
        return gson.toJson(dataMap)
    }

    @TypeConverter
    fun fromString(value: String): Map<String, BigDecimal> {
        val turnsType = object : TypeToken<Map<String, BigDecimal>>() {}.type
        return Gson().fromJson<Map<String, BigDecimal>>(value, turnsType)
    }

}