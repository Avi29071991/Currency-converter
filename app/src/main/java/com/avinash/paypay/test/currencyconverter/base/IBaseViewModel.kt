package com.avinash.paypay.test.currencyconverter.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avinash.paypay.test.foundation.environment.AppInfo
import com.avinash.paypay.test.foundation.logging.Log
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

/**
 * Create ViewModels by Extending this class.
 *
 * @param INTENT [ViewIntent] Represents all actions/events a user can perform on the view.
 * This is used to pass user input/action to the ViewModel.
 * [INTENT] here means intention to change the state of our app by an intent.
 *
 * @param STATE [ViewState] ViewState should represent the current state of the view at any given time.
 * State is an immutable data structure. At any given moment,
 * we have just one state in our app, which represents a single source of truth.
 * The only way to change the state is to create a new one by triggering viewModel.dispatchIntent(intent).
 * We can create this model using Kotlin's data class.
 *
 */
abstract class IBaseViewModel<INTENT : ViewIntent, STATE : ViewState> : ViewModel() {
    private val intentChannel = Channel<INTENT>(Channel.UNLIMITED)
    private val mutableLiveData: MutableLiveData<STATE> = MutableLiveData()

    val viewState: LiveData<STATE>
        get() = mutableLiveData

    init {
        viewModelScope.launch {
            processIntents()
        }
    }

    fun updateLiveData(state: STATE) {
        viewModelScope.launch {
            mutableLiveData.value = state
        }
    }

    fun dispatchIntent(intent: INTENT) {
        if (!viewState.hasObservers()) {
            if (!AppInfo.isReleaseBuild()) {
                throw NoObserverAttachedException("No LiveData observer attached.")
            }
        }

        Log.debug("processing viewIntent: $intent")
        intentChannel.offer(intent)
    }

    private suspend fun processIntents() {
        intentChannel.consumeAsFlow().collect {
            handleIntents(it)
        }
    }

    /**
     * To be overridden by the Implementation class to handle Intent
     */
    protected abstract suspend fun handleIntents(intent: INTENT)
}