package com.petnagy.superexchange.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Database
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.Update
import com.petnagy.superexchange.data.HistoryRate
import com.petnagy.superexchange.data.LatestRate
import io.reactivex.Maybe

/***
 * Database class.
 */
@Database(entities = [(LatestRate::class), (HistoryRate::class)], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun latestRateDao(): LatestRateDao

    abstract fun historyRateDao(): HistoryRateDao
}

/***
 * Dao object for Room at the case of LatestRate.
 */
@Dao
interface LatestRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: LatestRate)

    @Update
    fun update(item: LatestRate)

    @Delete
    fun delete(item: LatestRate): Int

    @Query("SELECT * FROM latest_rate WHERE base = :baseCurrency AND date = :date")
    fun query(baseCurrency: String, date: String): Maybe<LatestRate>
}

@Dao
interface HistoryRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: HistoryRate)

    @Update
    fun update(item: HistoryRate)

    @Delete
    fun delete(item: HistoryRate): Int

    @Query("SELECT * FROM history_rate WHERE base = :baseCurrency AND date = :date")
    fun query(baseCurrency: String, date: String): Maybe<HistoryRate>
}
