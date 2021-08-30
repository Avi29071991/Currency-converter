package com.avinash.paypay.test.currencyconverter.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * Create Activities by Extending this class.
 *
 * @param VB [ViewBinding] class for this activity
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
 * @param VM Respective ViewModel class for this activity which extends [IBaseViewModel]
 */
abstract class IBaseActivity<
    VB : ViewBinding,
    INTENT : ViewIntent,
    STATE : ViewState,
    VM : IBaseViewModel<INTENT, STATE>> : AppCompatActivity(), IViewRenderer<STATE> {

    protected lateinit var viewBinding: VB

    /**
     * Holds the mutable [STATE] for the [IBaseFragment]. This mutable state is only updated from the within the
     * [IBaseFragment]'s viewModel stateflow change observer.
     */
    private lateinit var viewState: STATE

    /**
     * Holds the current view state [STATE]
     */
    val state get() = viewState

    /**
     * Abstract [IBaseViewModel] to be overridden by implementation class.
     *
     * The viewModel can be provided using following snippet
     *
     * ```
     * override val viewModel: MainActVM by viewModels()
     * ```
     */
    abstract val viewModel: VM

    abstract fun setViewBinding(layoutInflater: LayoutInflater): VB

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = setViewBinding(layoutInflater)
        setContentView(viewBinding.root)

        viewModel.viewState.observe(this) {
            viewState = it
            render(it)
        }
    }
}
