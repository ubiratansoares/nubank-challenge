package br.ufs.hiring.nubankchallenge.uitests

import android.support.test.runner.AndroidJUnit4
import android.text.SpannableString
import br.ufs.hiring.nubankchallenge.chargeback.ChargebackActivity
import br.ufs.hiring.nubankchallenge.uitests.robots.errorFeedback
import br.ufs.hiring.nubankchallenge.uitests.robots.expectChargebackScreenAsSuch
import br.ufs.hiring.nubankchallenge.uitests.robots.webServiceSimulation
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
        webServiceSimulation {
            serversProbablyDown()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch {
            noInformationDisplayed()
            loadingIndicatorHidden()
            actionButtonsUnavailable()
        }

        errorFeedback {
            serverErrorReported()
        }
    }

    @Test fun acceptUndesiredResponseOnChargebackRetrieve() {
        webServiceSimulation {
            mysteriousClientError()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch {
            noInformationDisplayed()
            loadingIndicatorHidden()
            actionButtonsUnavailable()
        }

        errorFeedback {
            undesiredResponseReported()
        }
    }

    @Test fun acceptInternetErrorOnChargebackRetrieve() {

        webServiceSimulation {
            internetIssue()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch {
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

        webServiceSimulation {
            chargebackRetrieved()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch {

            loadingIndicatorHidden()
            actionButtonsAvailable()
            verifyContentsWith(expected)

            creditcard {
                verifyCreditcardAs(CreditcardState.UnblockedByDefault)
            }

            errorFeedback {
                noErrorFeedbackGiven()
            }
        }

    }

    @Test fun acceptChargebackRetrievedWithPreventiveCardBlocking() {

        val expected = expectedScreenInfo().copy(creditcardState = CreditcardState.BlockedBySystem)

        webServiceSimulation {
            chargebackRetrievedAndCreditcardBlockedPreventively()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch {

            loadingIndicatorHidden()
            actionButtonsAvailable()
            verifyContentsWith(expected)

            creditcard {
                verifyCreditcardAs(CreditcardState.BlockedBySystem)
            }

            errorFeedback {
                noErrorFeedbackGiven()
            }
        }

    }

    @Test fun acceptUserCanCancelChargeback() {
        webServiceSimulation {
            chargebackRetrieved()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch { leaveChargeback() }
        assertTrue(launcher.activity.isFinishing)
    }

    @Test fun acceptUserCanBlockCreditcard() {
        webServiceSimulation {
            chargebackRetrieved()
            creditcardBlocksWithSuccess()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch {

            creditcard {
                verifyCreditcardAs(CreditcardState.UnblockedByDefault)
                performCreditcardBlocking()
                verifyCreditcardAs(CreditcardState.BlockedByUser)
            }
        }

    }

    @Test fun acceptUserCanUnblockCreditcardWhenPreventivelyBlocked() {
        webServiceSimulation {
            chargebackRetrievedAndCreditcardBlockedPreventively()
            creditcardUnblocksWithSuccess()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch {

            creditcard {
                verifyCreditcardAs(CreditcardState.BlockedBySystem)
                performCreditcardUnblocking()
                verifyCreditcardAs(CreditcardState.UnblockedByUser)
            }
        }
    }

    @Test fun acceptUserCreditcardOperationMayFailDueInternetError() {
        webServiceSimulation {
            chargebackRetrieved()
            creditcardBlocksFailsWithInternetError()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch {

            creditcard {
                verifyCreditcardAs(CreditcardState.UnblockedByDefault)
                performCreditcardBlocking()
                internetErrorReported(launcher.activity.window)
                verifyCreditcardAs(CreditcardState.UnblockedByDefault)
            }
        }

    }

    @Test fun acceptUserCreditcardOperationMayFailDueSystemError() {
        webServiceSimulation {
            chargebackRetrievedAndCreditcardBlockedPreventively()
            creditcardUnblocksFailsWithInfrastructureError()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch {

            creditcard {
                verifyCreditcardAs(CreditcardState.BlockedBySystem)
                performCreditcardUnblocking()
                infrastructureErrorReported(launcher.activity.window)
                verifyCreditcardAs(CreditcardState.BlockedBySystem)
            }
        }

    }

    @Test fun acceptUserCanSubmitNewChargeback() {
        webServiceSimulation {
            chargebackRetrieved()
            reclaimedWithSuccess()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch {

            atChargebackSubmission {
                checkSuccess()
                closesScreenWhenDone(launcher.activity)
            }
        }

    }

    @Test fun acceptChargebackReclaimFailingWithInternetError() {
        webServiceSimulation {
            chargebackRetrieved()
            reclaimFailsWithInternetError()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch {

            atChargebackSubmission {
                internetErrorReported()
                doesNotCloseScreenWhenDone(launcher.activity)
            }
        }
    }

    @Test fun acceptChargebackReclaimFailingWithInfrastructureError() {
        webServiceSimulation {
            chargebackRetrieved()
            reclaimFailsWithInfrastructureError()
        }

        launcher.startScreen()

        expectChargebackScreenAsSuch {

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