package br.ufs.hiring.nubankchallenge.chargeback

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import br.ufs.hiring.nubankchallenge.R
import br.ufs.hiring.nubankchallenge.factories.PresentationFactory
import br.ufs.hiring.nubankchallenge.util.action
import br.ufs.hiring.nubankchallenge.util.colorForActionText
import br.ufs.hiring.nubankchallenge.util.screenProvider
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue
import br.ufs.nubankchallenge.core.presentation.chargeback.ChargebackScreenModel
import br.ufs.nubankchallenge.core.presentation.chargeback.LockpadState
import br.ufs.nubankchallenge.core.presentation.errorstate.ErrorStateView
import br.ufs.nubankchallenge.core.presentation.loading.LoadingView
import br.ufs.nubankchallenge.core.presentation.networking.NetworkingErrorView
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.activity_chargeback.*
import kotlinx.android.synthetic.main.view_error_feedback.*
import kotlinx.android.synthetic.main.view_lockpad_state.*

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class ChargebackActivity : AppCompatActivity(),
        LoadingView, NetworkingErrorView, ErrorStateView {

    val screen by screenProvider { PresentationFactory.chargebackScreen() }
    val presenter by lazy { PresentationFactory.behaviorsPresenter(this) }

    lateinit var subscription: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chargeback)
        reasonsView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        retrieveChargeback()
    }

    override fun onDestroy() {
        subscription.dispose()
        super.onDestroy()
    }

    override fun showLoading() = Action { loadingChargeback.visibility = View.VISIBLE }

    override fun hideLoading() = Action { loadingChargeback.visibility = View.GONE }

    override fun showErrorState(error: InfrastructureError) = Action {
        showFeedback(error)

        when (error) {
            is InfrastructureError.UndesiredResponse ->
                callToAction(
                        R.string.snacktext_undesired_response,
                        { finish() },
                        R.string.snackaction_done
                )
            is InfrastructureError.RemoteSystemDown ->
                callToAction(
                        R.string.snacktext_servers_down,
                        { forceScreenUpdate() }
                )
        }
    }

    override fun reportNetworkingError(issue: NetworkingIssue) = Action {
        showFeedback(issue)
        callToAction(
                R.string.snacktext_internet_connection,
                { forceScreenUpdate() }
        )
    }

    override fun hideErrorState() = Action { clearErrorFeedback() }

    private fun retrieveChargeback(forceUpdate: Boolean = false) {
        subscription = screen.chargebackOptions(forceUpdate)
                .compose(presenter)
                .subscribe(
                        { fillChargeback(it as ChargebackScreenModel) },
                        { Log.e("Chargeback", "Error -> $it") },
                        { Log.v("Chargeback", "Loaded with success") }
                )
    }

    private fun fillChargeback(model: ChargebackScreenModel) {
        reasonsView.adapter = null
        showChargebackViews()
        chargebackTitleLabel.text = model.screenTitle

        userCommentInput.apply {
            hint = model.commentHint
            onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                hint = if (hasFocus) "" else model.commentHint
            }
        }

        fillCreditcardInfo(model.lockpadState)
        reasonsView.adapter = ReclaimReasonsAdapter(model.reasons)
    }

    private fun fillCreditcardInfo(lockpadState: LockpadState) {
        lockpadView.setActualLockingState(lockpadState)
    }


    private fun showChargebackViews() {
        View.VISIBLE.let {
            lockpadView.visibility = it
            screenTitle.visibility = it
            chargebackContainer.visibility = it
            actionsContainer.visibility = it
        }
    }

    private fun showFeedback(error: Throwable) {
        feedbackContainer.apply {
            visibility = View.VISIBLE
            setState(error)
        }
    }

    private fun callToAction(
            callToActionText: Int,
            retryAction: (Any) -> Unit,
            retryText: Int = R.string.snackaction_retry) {

        Snackbar.make(chargebackScreenRoot, callToActionText, Snackbar.LENGTH_INDEFINITE)
                .action(retryText, retryAction)
                .colorForActionText(R.color.gray_call_to_action)
                .show()
    }

    private fun forceScreenUpdate() {
        releaseSubscriptions()
        clearErrorFeedback()
        retrieveChargeback(forceUpdate = true)
    }

    private fun clearErrorFeedback() {
        feedbackContainer.visibility = View.GONE
        feedbackContainer.reset()
    }

    private fun releaseSubscriptions() {
        subscription.dispose()
    }
}