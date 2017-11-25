package br.ufs.hiring.nubankchallenge.chargeback

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
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

    override fun showLoading() = Action { submissionProgressBar.visibility = View.VISIBLE }

    override fun hideLoading() = Action { submissionProgressBar.visibility = View.INVISIBLE }

    override fun showErrorState(error: InfrastructureError) = Action {
        feedback(R.string.toast_error_at_operation)
    }

    override fun reportNetworkingError(issue: NetworkingIssue) = Action {
        feedback(R.string.toast_internet_error)
    }

    override fun hideErrorState() = Action {
        // Nothing to do
    }

    fun closeScreenWith(closingAction: (Unit) -> Unit) {
        submissionDone.apply {
            visibility = View.VISIBLE
            isEnabled = true
            setOnClickListener { closingAction.invoke(Unit) }
        }

        submissionTitleLabel.text = context.getString(R.string.title_chargeback_submission_success)
        submissionMessageLabel.text = resources.getString(R.string.message_submission_success)
    }

    private fun feedback(messageId: Int) {
        Toast.makeText(context, messageId, Toast.LENGTH_LONG).show()
    }


}