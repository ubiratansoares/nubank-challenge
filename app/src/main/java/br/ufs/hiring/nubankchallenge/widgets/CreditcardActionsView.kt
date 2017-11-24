package br.ufs.hiring.nubankchallenge.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import br.ufs.hiring.nubankchallenge.R
import br.ufs.hiring.nubankchallenge.factories.PresentationFactory
import br.ufs.hiring.nubankchallenge.util.compoundDrawableLeft
import br.ufs.hiring.nubankchallenge.util.screenProvider
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue
import br.ufs.nubankchallenge.core.presentation.chargeback.ChargebackScreenModel
import br.ufs.nubankchallenge.core.presentation.chargeback.LockpadState
import br.ufs.nubankchallenge.core.presentation.chargeback.LockpadState.*
import br.ufs.nubankchallenge.core.presentation.errorstate.ErrorStateView
import br.ufs.nubankchallenge.core.presentation.loading.LoadingView
import br.ufs.nubankchallenge.core.presentation.networking.NetworkingErrorView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.view_lockpad_state.view.*

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class CreditcardActionsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr),
        LoadingView, NetworkingErrorView, ErrorStateView {

    private val screen by screenProvider { PresentationFactory.chargebackScreen() }
    private val presenter by lazy { PresentationFactory.behaviorsPresenter(this) }
    private val subscriptions by lazy { CompositeDisposable() }

    private var actualState: LockpadState = UnlockedByDefault

    init {
        setOnClickListener {
            when (actualState) {
                is LockedBySystem -> perform(screen.unblockCreditcard())
                is UnlockedByDefault -> perform(screen.blockCreditcard())
                is LockedByUser -> perform(screen.unblockCreditcard())
                is UnlockedByUser -> perform(screen.blockCreditcard())
            }
        }
    }

    override fun showLoading() = Action { lockOperationLoading.visibility = View.VISIBLE }

    override fun hideLoading() = Action { lockOperationLoading.visibility = View.GONE }

    override fun showErrorState(error: InfrastructureError) = Action {
        feedback(R.string.toast_error_at_operation)
        applyActualState()
    }

    override fun reportNetworkingError(issue: NetworkingIssue) = Action {
        feedback(R.string.toast_internet_error)
        applyActualState()
    }

    override fun hideErrorState() = Action {
        // Nothing to do
    }

    override fun onDetachedFromWindow() {
        subscriptions.clear()
        super.onDetachedFromWindow()
    }

    fun setActualLockingState(newState: LockpadState) {
        actualState = newState
    }

    private fun perform(operation: Observable<ChargebackScreenModel>) {
        val subscription = operation
                .doOnSubscribe { toProcessingState() }
                .compose(presenter)
                .subscribe(
                        { updateState(it as ChargebackScreenModel) },
                        { Log.e("BlockOrUnblock", "Error -> $it") },
                        { Log.v("BlockOrUnblock", "Done") }
                )

        subscriptions.add(subscription)
    }

    private fun updateState(model: ChargebackScreenModel) {
        val newState = model.lockpadState
        actualState = newState
        applyActualState()
    }

    private fun applyActualState() {
        lockpadLabel.apply {
            text = resources.getString(actualState.disclaimerResource)
            compoundDrawableLeft(actualState.lockPadImage)
        }
    }

    private fun toProcessingState() {
        lockpadLabel.text = resources.getString(R.string.message_wait_for_operation)
    }

    private fun feedback(messageId: Int) {
        Toast.makeText(context, messageId, Toast.LENGTH_LONG).show()
    }

}