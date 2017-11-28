package br.ufs.hiring.nubankchallenge.tests.chargeback

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import br.ufs.hiring.nubankchallenge.R
import br.ufs.hiring.nubankchallenge.chargeback.ChargebackSubmissionView
import br.ufs.hiring.nubankchallenge.util.typedInflation
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.RemoteSystemDown
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue.InternetUnreachable
import com.nhaarman.mockito_kotlin.*
import kotlinx.android.synthetic.main.view_chargeback_submission.view.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 *
 * Created by @ubiratanfsoares
 *
 */

@RunWith(RobolectricTestRunner::class)
class ChargebackSubmissionViewTests {

    val resources by lazy { RuntimeEnvironment.application.resources }

    lateinit var view: ChargebackSubmissionView
    lateinit var successCallback: (Unit) -> Unit
    lateinit var errorCallback: (Unit) -> Unit

    @Before fun `before each test`() {
        val activity = Robolectric.buildActivity(Activity::class.java)
                .create()
                .get()

        val inflater = LayoutInflater.from(activity)
        view = inflater.typedInflation(R.layout.view_chargeback_submission)
        successCallback = mock()
        errorCallback = mock()
        view.configureWith(successCallback, errorCallback)
    }

    @Test fun `should integrate delivery actions for progress bar`() {

        view.showLoading().run()
        `assert progress bar visibility as`(View.VISIBLE)

        view.hideLoading().run()
        `assert progress bar visibility as`(View.INVISIBLE)
    }

    @Test fun `should use hide error callback as reset condition`() {
        view.hideErrorState()
        assertThat(view.submissionDone.visibility).isEqualTo(View.INVISIBLE)
        assertThat(view.submissionDone.isEnabled).isFalse()
    }

    @Test fun `should integrate delivery action for infrastructure error feedback`() {
        view.showErrorState(RemoteSystemDown).run()
        `assert title message as`(R.string.title_chargeback_submission_error)
        `assert body message as`(R.string.toast_error_at_operation)
        `verify error callback dispatched at close click`()
    }

    @Test fun `should integrate delivery action for internet error feedback`() {
        view.reportNetworkingError(InternetUnreachable).run()
        `assert title message as`(R.string.title_chargeback_submission_error)
        `assert body message as`(R.string.toast_internet_error)
        `verify error callback dispatched at close click`()
    }

    @Test fun `should deliver action for success display`() {
        view.displaySuccess()
        `assert title message as`(R.string.title_chargeback_submission_success)
        `assert body message as`(R.string.message_submission_success)
        `verify success callback dispatched at close click`()
    }

    private fun `verify success callback dispatched at close click`() {
        view.submissionDone.performClick()
        verify(successCallback, times(1)).invoke(Unit)
        verifyNoMoreInteractions(successCallback)
        verifyZeroInteractions(errorCallback)
    }

    private fun `verify error callback dispatched at close click`() {
        view.submissionDone.performClick()
        verify(errorCallback, times(1)).invoke(Unit)
        verifyNoMoreInteractions(errorCallback)
        verifyZeroInteractions(successCallback)
    }

    private fun `assert progress bar visibility as`(given: Int) {
        val progress = view.submissionProgressBar
        assertThat(progress.visibility).isEqualTo(given)
    }

    private fun `assert title message as`(givenString: Int) {
        val expected = resources.getString(givenString)
        assertThat(view.submissionTitleLabel.text).isEqualTo(expected)
    }

    private fun `assert body message as`(givenString: Int) {
        val expected = resources.getString(givenString)
        assertThat(view.submissionMessageLabel.text).isEqualTo(expected)
    }

}