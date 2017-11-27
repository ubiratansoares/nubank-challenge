package br.ufs.hiring.nubankchallenge.chargeback

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import br.ufs.hiring.nubankchallenge.R
import br.ufs.hiring.nubankchallenge.factories.PresentationFactory
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue
import br.ufs.nubankchallenge.core.presentation.errorstate.ErrorStateView
import br.ufs.nubankchallenge.core.presentation.loading.LoadingView
import br.ufs.nubankchallenge.core.presentation.networking.NetworkingErrorView
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.view_chargeback_submission.view.*

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class ChargebackSubmissionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr),
        LoadingView, NetworkingErrorView, ErrorStateView {

    val presenter by lazy { PresentationFactory.behaviorsPresenter(this) }

    lateinit var callbackAtSuccess: (Unit) -> Unit
    lateinit var callbackAtError: (Unit) -> Unit

    override fun showLoading() = Action { submissionProgressBar.visibility = View.VISIBLE }

    override fun hideLoading() = Action { submissionProgressBar.visibility = View.INVISIBLE }

    override fun showErrorState(error: InfrastructureError) = Action {
        errorFeedback(R.string.toast_error_at_operation)
    }

    override fun reportNetworkingError(issue: NetworkingIssue) = Action {
        errorFeedback(R.string.toast_internet_error)
    }

    override fun hideErrorState() = Action {
        submissionDone.apply {
            visibility = View.INVISIBLE
            isEnabled = false
        }
    }

    fun configureWith(success: (Unit) -> Unit, error: (Unit) -> Unit) {
        callbackAtSuccess = success
        callbackAtError = error
    }

    fun displaySuccess() {
        enableCallToAction(callbackAtSuccess)
        submissionTitleLabel.text = context.getString(R.string.title_chargeback_submission_success)
        submissionMessageLabel.text = resources.getString(R.string.message_submission_success)
    }

    private fun errorFeedback(messageId: Int) {
        enableCallToAction(callbackAtError)
        submissionTitleLabel.text = "Contestação não recebida"
        submissionMessageLabel.text = resources.getString(messageId)
    }

    private fun enableCallToAction(callback: (Unit) -> Unit) {
        submissionDone.apply {
            visibility = View.VISIBLE
            isEnabled = true
            setOnClickListener { callback.invoke(Unit) }
        }
    }

}