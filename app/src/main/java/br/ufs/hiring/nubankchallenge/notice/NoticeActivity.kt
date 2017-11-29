package br.ufs.hiring.nubankchallenge.notice

import android.app.ActivityOptions.makeCustomAnimation
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_INDEFINITE
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import br.ufs.hiring.nubankchallenge.R
import br.ufs.hiring.nubankchallenge.chargeback.ChargebackActivity
import br.ufs.hiring.nubankchallenge.factories.PresentationFactory
import br.ufs.hiring.nubankchallenge.util.action
import br.ufs.hiring.nubankchallenge.util.colorForActionText
import br.ufs.hiring.nubankchallenge.util.screenProvider
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.RemoteSystemDown
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue
import br.ufs.nubankchallenge.core.presentation.errorstate.ErrorStateView
import br.ufs.nubankchallenge.core.presentation.loading.LoadingView
import br.ufs.nubankchallenge.core.presentation.networking.NetworkingErrorView
import br.ufs.nubankchallenge.core.presentation.notice.NoticeScreenModel
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.activity_notice.*
import kotlinx.android.synthetic.main.view_error_feedback.*

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class NoticeActivity : AppCompatActivity(),
        LoadingView, NetworkingErrorView, ErrorStateView {

    val screen by screenProvider { PresentationFactory.noticeScreen() }
    val presenter by lazy { PresentationFactory.behaviorsPresenter(this) }

    lateinit var subscription: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)
    }

    override fun onResume() {
        super.onResume()
        retrieveNotice()
    }

    override fun onDestroy() {
        subscription.dispose()
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.slidedown)
    }

    override fun showLoading(): Action {
        return Action { loadingIndicator.visibility = View.VISIBLE }
    }

    override fun hideLoading(): Action {
        return Action { loadingIndicator.visibility = View.GONE }
    }

    override fun showErrorState(error: InfrastructureError): Action {
        showFeedback(error)

        return Action {
            when (error) {
                is UndesiredResponse ->
                    callToAction(
                            R.string.snacktext_undesired_response,
                            { finish() },
                            R.string.snackaction_done
                    )
                is RemoteSystemDown ->
                    callToAction(
                            R.string.snacktext_servers_down,
                            { forceScreenUpdate() }
                    )
            }
        }
    }

    override fun reportNetworkingError(issue: NetworkingIssue): Action {
        showFeedback(issue)

        return Action {
            callToAction(
                    R.string.snacktext_internet_connection,
                    { forceScreenUpdate() }
            )
        }
    }

    override fun hideErrorState(): Action {
        return Action { clearErrorFeedback() }
    }

    private fun showFeedback(error: Throwable) {
        feedbackContainer.visibility = View.VISIBLE
        feedbackContainer.setState(error)
    }

    private fun retrieveNotice(forceUpdate: Boolean = false) {
        subscription = screen.retrieveNotice(forceUpdate)
                .compose(presenter)
                .subscribe(
                        { fillNotice(it as NoticeScreenModel) },
                        { Log.e("Notice", "Error -> $it") },
                        { Log.v("Notice", "Loaded with success") }
                )
    }

    fun fillNotice(model: NoticeScreenModel) {
        showNoticeViews()
        proceedButton.text = model.proceedButtonLabel
        cancelButton.text = model.cancelButtonLabel
        noticeTitleLabel.text = model.title
        noticeDescriptionLabel.text = model.formattedDescription

        proceedButton.setOnClickListener { proceedToChargeback() }
        cancelButton.setOnClickListener { finish() }
    }

    private fun showNoticeViews() {
        appbar.visibility = View.VISIBLE
        noticeDescriptionLabel.visibility = View.VISIBLE
        buttonsContainer.visibility = View.VISIBLE
    }

    private fun proceedToChargeback() {
        val noAnimations =
                makeCustomAnimation(
                this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        ).toBundle()

        val toChargeback = Intent(this, ChargebackActivity::class.java)
        startActivity(toChargeback, noAnimations)
    }

    private fun callToAction(
            callToActionText: Int,
            retryAction: (Any) -> Unit,
            retryText: Int = R.string.snackaction_retry) {

        Snackbar.make(screenRoot, callToActionText, LENGTH_INDEFINITE)
                .action(retryText, retryAction)
                .colorForActionText(R.color.gray_call_to_action)
                .show()
    }

    private fun forceScreenUpdate() {
        releaseSubscriptions()
        clearErrorFeedback()
        retrieveNotice(forceUpdate = true)
    }

    private fun clearErrorFeedback() {
        feedbackContainer.visibility = View.GONE
        feedbackContainer.reset()
    }

    private fun releaseSubscriptions() {
        subscription.dispose()
    }
}