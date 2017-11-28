package br.ufs.hiring.nubankchallenge.uitests

import android.support.test.runner.AndroidJUnit4
import br.ufs.hiring.nubankchallenge.factories.FakeResponses
import br.ufs.hiring.nubankchallenge.factories.WebServiceFactory
import br.ufs.hiring.nubankchallenge.notice.NoticeActivity
import br.ufs.hiring.nubankchallenge.uitests.robots.errorFeedback
import br.ufs.hiring.nubankchallenge.uitests.robots.noticeInformation
import br.ufs.hiring.nubankchallenge.uitests.util.ScreenLauncher
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoticeAcceptanceTests {

    @Rule @JvmField val launcher = ScreenLauncher(NoticeActivity::class)

    private val mockRestAPI by lazy { WebServiceFactory.webservice }

    @Test fun acceptServerDownScenario() {
        whenever(mockRestAPI.chargebackNotice()).thenReturn(FakeResponses.serversMaybeDown())

        launcher.startScreen()

        noticeUnavailable()
        errorFeedback {
            serverErrorReported()
        }
    }

    @Test fun acceptUndesiredResponseScenario() {

        whenever(mockRestAPI.chargebackNotice()).thenReturn(FakeResponses.mysteriousClientError())

        launcher.startScreen()

        noticeUnavailable()
        errorFeedback {
            undesiredResponseReported()
        }
    }

    @Test fun acceptNetworkingErrorScenario() {

        whenever(mockRestAPI.chargebackNotice()).thenReturn(FakeResponses.someNetworkingError())

        launcher.startScreen()
        noticeUnavailable()
        errorFeedback {
            internetIssueReported()
        }
    }

    @Test fun acceptNoticeRetrievedScenario() {
        whenever(mockRestAPI.chargebackNotice()).thenReturn(FakeResponses.noticeRetrived())
        launcher.startScreen()

        verifyNoticeDisplayed()
    }

    @Test fun shouldAcceptErrorRecovering() {
        whenever(mockRestAPI.chargebackNotice())
                .thenReturn(FakeResponses.someNetworkingError(), FakeResponses.noticeRetrived())
        launcher.startScreen()
        noticeInformation { retry() }
        verifyNoticeDisplayed()
    }

    @Test fun acceptUserCancelChargeback() {
        whenever(mockRestAPI.chargebackNotice())
                .thenReturn(FakeResponses.noticeRetrived())

        launcher.startScreen()

        noticeInformation { userGivesUpOnChargeback() }
        assertTrue(launcher.activity.isFinishing)
    }

    private fun verifyNoticeDisplayed() {

        errorFeedback {
            noErrorFeedbackGiven()
        }

        noticeInformation {
            loadingIndicatorHidden()
            actionButtonsAvailable()
            noticeDelivered()
            noticeTitle("Antes de continuar")
            primaryActionButton("Continuar")
            secondaryActionButton("Fechar")
        }
    }

    private fun noticeUnavailable() {
        noticeInformation {
            noInformationDisplayed()
            loadingIndicatorHidden()
            actionButtonsUnavailable()
        }
    }

}


