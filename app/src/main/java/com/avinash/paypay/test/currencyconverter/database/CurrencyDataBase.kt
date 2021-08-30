package com.avinash.paypay.test.currencyconverter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Class extending [RoomDatabase] used to initialise our database instance.
 * This will also provide instance of [CurrencyDao] dao which will help us in performing
 * database operations like Fetch, Insert, Update and Delete
 */
@Database(entities = [CurrencyEntity::class], version = 1)
abstract class CurrencyDataBase: RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    companion object {
        private var instance: CurrencyDataBase? = null
        @Synchronized
        fun get(context: Context): CurrencyDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                    CurrencyDataBase::class.java, "CurrencyDataBase")
                .build()
            }
            return instance!!
        }
    }
}