package com.avinash.paypay.test.currencyconverter.di

import android.content.Context
import androidx.room.Room
import com.avinash.paypay.test.currencyconverter.database.CurrencyDao
import com.avinash.paypay.test.currencyconverter.database.CurrencyDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module for providing instances of database and dao
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    /**
     * Provides instance of room database required for our currency application
     */
    @Provides
    @Singleton
    fun providesCurrencyDatabase(@ApplicationContext context: Context): CurrencyDataBase {
        return Room.databaseBuilder(
            context,
            CurrencyDataBase::class.java,
            "CurrencyDataBase"
        ).build()
    }

    /**
     * Provides instance of Dao for our currency database
     */
    @Provides
    @Singleton
    fun providesCurrencyDao(dataBase: CurrencyDataBase): CurrencyDao {
        return dataBase.currencyDao()
    }
}