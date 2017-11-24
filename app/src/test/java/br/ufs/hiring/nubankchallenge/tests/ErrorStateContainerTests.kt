package br.ufs.hiring.nubankchallenge.tests

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import br.ufs.hiring.nubankchallenge.R
import br.ufs.hiring.nubankchallenge.notice.NoticeActivity
import br.ufs.hiring.nubankchallenge.widgets.ErrorStateContainer
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue
import kotlinx.android.synthetic.main.view_error_feedback.view.*
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
class ErrorStateContainerTests {

    lateinit var container: ErrorStateContainer

    val context by lazy { RuntimeEnvironment.application }

    @Before fun `before each test`() {
        val activity = Robolectric.buildActivity(NoticeActivity::class.java)
                .create()
                .get()

        val inflater = LayoutInflater.from(activity)
        container = inflater.inflate(R.layout.view_error_feedback, null) as ErrorStateContainer
    }

    @Test fun `should reset properly`() {
        container.reset()
        assertThat(container.errorImage.drawable).isNull()
        `error label should have text with`(R.string.emtpy_string)
    }

    @Test fun `should display error from networking properly`() {
        container.setState(NetworkingIssue.OperationTimeout)
        `error image should have drawable with`(R.drawable.img_network_offline)
        `error label should have text with`(R.string.error_internet_connection)
    }

    @Test fun `should display error for remote system down properly`() {
        container.setState(InfrastructureError.RemoteSystemDown)
        `error image should have drawable with`(R.drawable.img_servers_down)
        `error label should have text with`(R.string.error_server_down)
    }

    @Test fun `should display error for undesired responde properly`() {
        container.setState(InfrastructureError.UndesiredResponse)
        `error image should have drawable with`(R.drawable.img_undesired_response)
        `error label should have text with`(R.string.error_undesired_response)
    }

    private fun `error label should have text with`(errorString: Int) {
        val feedback = context.getString(errorString)
        assertThat(container.errorMessage.text).isEqualTo(feedback)
    }

    private fun `error image should have drawable with`(imageResource: Int) {
        val target = ContextCompat.getDrawable(context, imageResource)
        assertThat(container.errorImage.drawable).isEqualTo(target)
    }
}