package com.petnagy.superexchange.room

import android.arch.persistence.room.*
import com.petnagy.superexchange.data.LatestRate
import io.reactivex.Maybe

/***
 * Database class.
 */
@Database(entities = [(LatestRate::class)], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun latestRateDao(): LatestRateDao
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