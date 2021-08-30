package com.avinash.paypay.test.currencyconverter.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class which provides column details for our table in database
 */
@Entity
data class CurrencyEntity(
    @PrimaryKey
    val currencyCode: String,
    val currencyValue: Double? = null,
    val currencyName: String? = null
)