package br.ufs.hiring.nubankchallenge.tests.notice

import android.text.SpannableString
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import br.ufs.hiring.nubankchallenge.R
import br.ufs.hiring.nubankchallenge.notice.NoticeActivity
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.RemoteSystemDown
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue.InternetUnreachable
import br.ufs.nubankchallenge.core.presentation.notice.NoticeScreenModel
import kotlinx.android.synthetic.main.activity_notice.*
import kotlinx.android.synthetic.main.view_error_feedback.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

/**
 *
 * Created by @ubiratanfsoares
 *
 */
@RunWith(RobolectricTestRunner::class)
class NoticeActivityTests {

    lateinit var activity: NoticeActivity
    lateinit var loading: ProgressBar
    lateinit var feedbackContainer: View
    lateinit var titleLabel: TextView
    lateinit var descriptionLabel: TextView
    lateinit var proceedButton: Button
    lateinit var cancelButton: Button
    lateinit var errorImage: ImageView
    lateinit var errorLabel: TextView

    @Before fun `before each test`() {
        activity = Robolectric.buildActivity(NoticeActivity::class.java).create().get()
        loading = activity.loadingIndicator
        feedbackContainer = activity.feedbackContainer
        titleLabel = activity.noticeTitleLabel
        descriptionLabel = activity.noticeDescriptionLabel
        proceedButton = activity.proceedButton
        cancelButton = activity.cancelButton
        errorImage = activity.errorImage
        errorLabel = activity.errorMessage
    }

    @Test fun `should setup views properly at screen creation`() {
        `loading should not be visible`()
        `error related views should not be visible`()
    }

    @Test fun `should delievery actions for progress bar control`() {
        activity.showLoading().run()
        assertThat(loading.visibility).isEqualTo(View.VISIBLE)

        activity.hideLoading().run()
        assertThat(loading.visibility).isEqualTo(View.GONE)
    }

    @Test fun `should delievery actions for remote servers down feedback`() {
        activity.showErrorState(RemoteSystemDown).run()
        `error feedback container should be visible`()
        `call to action should be visible with message`(R.string.snacktext_servers_down)

        activity.hideErrorState().run()
        `error views should be reseted`()
    }

    @Test fun `should delievery actions for underised response feedback`() {
        activity.showErrorState(UndesiredResponse).run()
        `error feedback container should be visible`()

        activity.hideErrorState().run()
        `error views should be reseted`()
    }

    @Test fun `should delievery actions for internet error report`() {
        activity.reportNetworkingError(InternetUnreachable).run()
        `error feedback container should be visible`()
        `call to action should be visible with message`(R.string.snacktext_internet_connection)

        activity.hideErrorState().run()
        `error views should be reseted`()
    }

    @Test fun `should fill notice when data arrives`() {
        val model = NoticeScreenModel(
                title = "Hello",
                formattedDescription = SpannableString("World"),
                proceedButtonLabel = "Continuar",
                cancelButtonLabel = "Fechar"
        )

        activity.fillNotice(model)

        assertThat(titleLabel.visibility).isEqualTo(View.VISIBLE)
        assertThat(titleLabel.text).isEqualTo(model.title)

        assertThat(descriptionLabel.visibility).isEqualTo(View.VISIBLE)
        assertThat(descriptionLabel.text).isEqualTo(model.formattedDescription)

        assertThat(proceedButton.visibility).isEqualTo(View.VISIBLE)
        assertThat(proceedButton.text).isEqualTo(model.proceedButtonLabel)

        assertThat(cancelButton.visibility).isEqualTo(View.VISIBLE)
        assertThat(cancelButton.text).isEqualTo(model.cancelButtonLabel)
    }
    
    private fun `call to action should be visible with message`(messageResource: Int) {
        val snackText = activity.findViewById<TextView>(R.id.snackbar_text)
        assertThat(snackText).isNotNull()

        val feedback = activity.getString(messageResource)
        assertThat(snackText.text).isEqualTo(feedback)
    }

    private fun `error views should be reseted`() {
        assertThat(feedbackContainer.visibility).isEqualTo(View.GONE)
        assertThat(errorLabel.text).isEqualTo("")
        assertThat(errorImage.drawable).isNull()
    }

    private fun `error feedback container should be visible`() {
        assertThat(feedbackContainer.visibility).isEqualTo(View.VISIBLE)
        assertThat(errorImage.visibility).isEqualTo(View.VISIBLE)
        assertThat(errorLabel.visibility).isEqualTo(View.VISIBLE)
    }

    private fun `error related views should not be visible`() {
        assertThat(feedbackContainer.visibility).isEqualTo(View.GONE)
    }

    private fun `loading should not be visible`() {
        assertThat(loading.visibility).isEqualTo(View.GONE)
    }

}