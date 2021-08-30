package com.avinash.paypay.test.currencyconverter

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.avinash.paypay.test.currencyconverter.base.IBaseActivity
import com.avinash.paypay.test.currencyconverter.databinding.ActivityMainBinding
import com.avinash.paypay.test.currencyconverter.viewintent.CurrencyIntent
import com.avinash.paypay.test.currencyconverter.viewmodel.CurrencyViewModel
import com.avinash.paypay.test.currencyconverter.viewstate.CurrencyState
import com.avinash.paypay.test.currencyconverter.worker.CurrencyWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : IBaseActivity<ActivityMainBinding,
        CurrencyIntent,
        CurrencyState,
        CurrencyViewModel>() {

    private var workManager: WorkManager? = null

    override val viewModel by viewModels<CurrencyViewModel>()

    override fun setViewBinding(
        layoutInflater: LayoutInflater
    ) = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enqueueWorkRequest()
    }

    override fun render(state: CurrencyState) {
        // No Implementation is required
    }

    private fun enqueueWorkRequest() {
        workManager = WorkManager.getInstance(this@MainActivity)

        // Create Network constraint
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        // Create periodic work request
        val periodicSyncDataWork =
            PeriodicWorkRequest.Builder(CurrencyWorker::class.java, 30, TimeUnit.MINUTES)
                .setConstraints(constraints) // setting a backoff on case the work needs to retry
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

        // Enqueue periodic work request
        workManager?.enqueueUniquePeriodicWork(
            SYNC_DATA_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,  //Existing Periodic Work policy
            periodicSyncDataWork //work request
        )
    }

    companion object {
        private const val SYNC_DATA_WORK_NAME = "Currency_Sync_Job"
    }
}