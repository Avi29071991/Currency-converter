package com.avinash.paypay.test.currencyconverter.base

/**
 * ViewState should represent the current state of the view at any given time.
 * State is an immutable data structure. At any given moment,
 * we have just one state in our app, which represents a single source of truth.
 * The only way to change the state is to create a new one by triggering viewModel.dispatchIntent(intent).
 * We can create this model using Kotlin's data class.
 */
interface ViewState
