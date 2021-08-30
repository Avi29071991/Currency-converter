package com.avinash.paypay.test.currencyconverter.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Dao class for our Currency data base class
 * This class will provide us the mechanism to perform various data base operations like
 * Insert, Delete, Update and Fetch
 */
@Dao
interface CurrencyDao {

    /**
     * Fetching all the records of [CurrencyEntity] saved in the currency table of our database
     * @return List of [CurrencyEntity]
     */
    @Query("SELECT * FROM CurrencyEntity ORDER BY currencyCode")
    suspend fun allCurrencies(): List<CurrencyEntity>

    /**
     * Inserts list of [CurrencyEntity] in currency table of our database
     * @param currencies List of currencies which needs to be inserted
     */
    @Insert
    suspend fun insertCurrencies(currencies: List<CurrencyEntity>)

    /**
     * Deletes a single record of mentioned currency from our database
     * @param currency [CurrencyEntity] record which needs to be deleted
     */
    @Delete
    suspend fun deleteCurrency(currency: CurrencyEntity)

    /**
     * Deletes all the records from our database
     */
    @Query("DELETE from CurrencyEntity")
   suspend fun deleteCurrencies()
}