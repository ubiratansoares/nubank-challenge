package br.ufs.hiring.nubankchallenge.uitests

import android.support.test.runner.AndroidJUnit4
import android.text.SpannableString
import br.ufs.hiring.nubankchallenge.chargeback.ChargebackActivity
import br.ufs.hiring.nubankchallenge.mocked.webServiceSimulatedWith
import br.ufs.hiring.nubankchallenge.uitests.robots.chargebackScreenIsSuchThat
import br.ufs.hiring.nubankchallenge.uitests.robots.errorFeedback
import br.ufs.hiring.nubankchallenge.uitests.util.ScreenLauncher
import br.ufs.nubankchallenge.core.presentation.chargeback.ChargebackScreenModel
import br.ufs.nubankchallenge.core.presentation.chargeback.CreditcardState
import br.ufs.nubankchallenge.core.presentation.chargeback.ReasonRowModel
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 *
 * Created by @ubiratanfsoares
 *
 */

@RunWith(AndroidJUnit4::class)
class ChargebackAcceptanceTests {

    @Rule @JvmField val launcher = ScreenLauncher(ChargebackActivity::class)

    @Test fun acceptRemoteSystemErrorOnChargebackRetrieve() {
        webServiceSimulatedWith {
            serversProbablyDownWhenRetrievingChargeback()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat {
            noInformationDisplayed()
            loadingIndicatorHidden()
            actionButtonsUnavailable()
        }

        errorFeedback {
            serverErrorReported()
        }
    }

    @Test fun acceptUndesiredResponseOnChargebackRetrieve() {
        webServiceSimulatedWith {
            undesiredResponseWhenRetrievingChargeback()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat {
            noInformationDisplayed()
            loadingIndicatorHidden()
            actionButtonsUnavailable()
        }

        errorFeedback {
            undesiredResponseReported()
        }
    }

    @Test fun acceptInternetErrorOnChargebackRetrieve() {

        webServiceSimulatedWith {
            internetIssueWhenRetrievingChargeback()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat {
            noInformationDisplayed()
            loadingIndicatorHidden()
            actionButtonsUnavailable()

            errorFeedback {
                internetIssueReported()
            }
        }
    }

    @Test fun acceptChargebackRetrieved() {

        val expected = expectedScreenInfo()

        webServiceSimulatedWith {
            chargebackRetrievedWithSuccess()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat {

            loadingIndicatorHidden()
            actionButtonsAvailable()
            verifyContentsWith(expected)

            onCreditcardInteraction {
                verifyCreditcardAs(CreditcardState.UnblockedByDefault)
            }

            errorFeedback {
                noErrorFeedbackGiven()
            }
        }

    }

    @Test fun acceptChargebackRetrievedWithPreventiveCardBlocking() {

        val expected = expectedScreenInfo().copy(
                creditcardState = CreditcardState.BlockedBySystem
        )

        webServiceSimulatedWith {
            chargebackRetrievedAndCreditcardBlockedPreventively()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat {

            loadingIndicatorHidden()
            actionButtonsAvailable()
            verifyContentsWith(expected)

            onCreditcardInteraction {
                verifyCreditcardAs(CreditcardState.BlockedBySystem)
            }

            errorFeedback {
                noErrorFeedbackGiven()
            }
        }

    }

    @Test fun acceptUserCanCancelChargeback() {
        webServiceSimulatedWith {
            chargebackRetrievedWithSuccess()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat { leaveChargeback() }
        assertTrue(launcher.activity.isFinishing)
    }

    @Test fun acceptUserCanBlockCreditcard() {
        webServiceSimulatedWith {
            chargebackRetrievedWithSuccess()
            creditcardBlocksWithSuccess()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat {

            onCreditcardInteraction {
                verifyCreditcardAs(CreditcardState.UnblockedByDefault)
                performCreditcardBlocking()
                verifyCreditcardAs(CreditcardState.BlockedByUser)
            }
        }

    }

    @Test fun acceptUserCanUnblockCreditcardEvenIfPreventivelyBlocked() {
        webServiceSimulatedWith {
            chargebackRetrievedAndCreditcardBlockedPreventively()
            creditcardUnblocksWithSuccess()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat {

            onCreditcardInteraction {
                verifyCreditcardAs(CreditcardState.BlockedBySystem)
                performCreditcardUnblocking()
                verifyCreditcardAs(CreditcardState.UnblockedByUser)
            }
        }
    }

    @Test fun acceptUserCreditcardOperationMayFailDueInternetError() {
        webServiceSimulatedWith {
            chargebackRetrievedWithSuccess()
            creditcardBlockFailsWithInternetError()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat {

            onCreditcardInteraction {
                verifyCreditcardAs(CreditcardState.UnblockedByDefault)
                performCreditcardBlocking()
                internetErrorReported(launcher.activity.window)
                verifyCreditcardAs(CreditcardState.UnblockedByDefault)
            }
        }

    }

    @Test fun acceptUserCreditcardOperationMayFailWithInfrastructureError() {
        webServiceSimulatedWith {
            chargebackRetrievedAndCreditcardBlockedPreventively()
            creditcardUnblockFailsWithInternetError()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat {

            onCreditcardInteraction {
                verifyCreditcardAs(CreditcardState.BlockedBySystem)
                performCreditcardUnblocking()
                internetErrorReported(launcher.activity.window)
                verifyCreditcardAs(CreditcardState.BlockedBySystem)
            }
        }

    }

    @Test fun acceptUserCanSubmitNewChargeback() {
        webServiceSimulatedWith {
            chargebackRetrievedWithSuccess()
            purchaseReclaimedWithSuccess()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat {

            atChargebackSubmission {
                reclaimSentWithSuccess()
                closesScreenWhenDone(launcher.activity)
            }
        }

    }

    @Test fun acceptChargebackReclaimMayFailWithInternetError() {
        webServiceSimulatedWith {
            chargebackRetrievedWithSuccess()
            purchaseReclaimFailsWithInternetError()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat {

            atChargebackSubmission {
                internetErrorReported()
                doesNotCloseScreenWhenDone(launcher.activity)
            }
        }
    }

    @Test fun acceptChargebackReclaimMayWithInfrastructureError() {
        webServiceSimulatedWith {
            chargebackRetrievedWithSuccess()
            purchaseReclaimFailsWithInfrastructureError()
        }

        launcher.startScreen()

        chargebackScreenIsSuchThat {

            atChargebackSubmission {
                infrastructureErrorReported()
                doesNotCloseScreenWhenDone(launcher.activity)
            }
        }
    }

    // Translated from fake responses
    private fun expectedScreenInfo() = ChargebackScreenModel(
            screenTitle = "Não reconheço essa compra",
            commentHint = SpannableString("Nos conte o que aconteceu"),
            creditcardState = CreditcardState.UnblockedByDefault,

            reasons = listOf(
                    ReasonRowModel(
                            "merchant_recognized",
                            "Não reconheço o vendedor"
                    ),
                    ReasonRowModel(
                            "card_in_possession",
                            "Está com o cartão em mãos?"
                    )

            )
    )
}