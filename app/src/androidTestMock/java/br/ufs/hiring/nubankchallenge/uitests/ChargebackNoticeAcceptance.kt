package br.ufs.hiring.nubankchallenge.uitests

import android.support.test.runner.AndroidJUnit4
import br.ufs.hiring.nubankchallenge.factories.TestScenario.*
import br.ufs.hiring.nubankchallenge.notice.ChargebackNoticeActivity
import br.ufs.hiring.nubankchallenge.uitests.robots.errorFeedback
import br.ufs.hiring.nubankchallenge.uitests.robots.noticeInformation
import br.ufs.hiring.nubankchallenge.uitests.util.ScenariosLauncher
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChargebackNoticeAcceptance {

    @Rule @JvmField val launcher = ScenariosLauncher(ChargebackNoticeActivity::class)

    @Test fun acceptServerDownScenario() {

        launcher.launchWithCenario(ServersDown)

        noticeUnavailable()
        errorFeedback {
            serverErrorReported()
        }
    }

    @Test fun acceptUndesiredResponseScenario() {

        launcher.launchWithCenario(ClientError)

        noticeUnavailable()
        errorFeedback {
            undesiredResponseReported()
        }
    }

    @Test fun acceptNetworkingErrorScenario() {

        launcher.launchWithCenario(NetworkingHeadache)

        noticeUnavailable()
        errorFeedback {
            internetIssueReported()
        }
    }

    @Test fun acceptNoticeRetrievedScenario() {
        launcher.launchWithCenario(ChargebackNoticeRetrieved)
        verifyNoticeDisplayed()
    }

    @Test fun shouldAcceptErrorRecovering() {
        launcher.launchWithCenario(NetworkingHeadache)
        errorFeedback { internetIssueReported() }

        launcher.nextScenario(ChargebackNoticeRetrieved)
        noticeInformation { retry() }
        verifyNoticeDisplayed()
    }

    @Test fun acceptUserCancelChargeback() {
        launcher.launchWithCenario(ChargebackNoticeRetrieved)
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


