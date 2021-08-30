package com.avinash.paypay.test.currencyconverter.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.avinash.paypay.test.currencyconverter.database.CurrencyDao
import com.avinash.paypay.test.currencyconverter.database.CurrencyEntity
import com.avinash.paypay.test.currencykotlin.datasource.CurrencyDataSource
import com.avinash.paypay.test.currencykotlin.model.CurrencyResult
import com.avinash.paypay.test.currencykotlin.model.LiveRatesCurrencyModel
import com.avinash.paypay.test.foundation.logging.Log
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Worker class used to fetch currency live rates from API .
 * This class will be triggered programmatically after 30 minutes to fetch
 * currency live rates and keep it updated in our database
 */
@HiltWorker
class CurrencyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workParams: WorkerParameters,
    private val currencyDao: CurrencyDao
): Worker(context, workParams) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun doWork(): Result {
        Log.debug("Starting Currency data fetch from Worker class")
        CurrencyDataSource.fetchLiveRateForSourceCurrency { result ->
            if (result is CurrencyResult.Value) {
                saveData(data = result.data)
            }
        }

        return Result.success()
    }

    private fun saveData(data: LiveRatesCurrencyModel?) {
        coroutineScope.launch {
            Log.debug("Saving currency data from Worker class")
            val currencies = data?.currencyQuotes?.map {
                CurrencyEntity(
                    currencyCode = it.currencyCode,
                    currencyValue = it.currencyRate.toDouble()
                )
            }

            currencyDao.deleteCurrencies()
            if (!currencies.isNullOrEmpty()) {
                currencyDao.insertCurrencies(currencies = currencies)
            }
        }
    }
}