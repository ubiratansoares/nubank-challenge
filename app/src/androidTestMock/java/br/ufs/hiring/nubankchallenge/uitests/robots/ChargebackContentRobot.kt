package br.ufs.hiring.nubankchallenge.uitests.robots

import br.ufs.hiring.nubankchallenge.R
import br.ufs.nubankchallenge.core.presentation.chargeback.ChargebackScreenModel
import com.schibsted.spain.barista.BaristaAssertions.*
import com.schibsted.spain.barista.BaristaClickActions.click

/**
 *
 * Created by @ubiratanfsoares
 *
 */

fun expectChargebackScreenAsSuch(func: ChargebackContentRobot.() -> Unit) =
        ChargebackContentRobot().apply { func() }

class ChargebackContentRobot {

    fun loadingIndicatorHidden(): ChargebackContentRobot {
        assertNotDisplayed(R.id.loadingChargeback)
        return this
    }

    fun actionButtonsUnavailable(): ChargebackContentRobot {
        assertNotDisplayed(R.id.actionsContainer)
        return this
    }

    fun actionButtonsAvailable(): ChargebackContentRobot {
        assertDisplayed(R.id.actionsContainer)
        return this
    }

    fun verifyContentsWith(expected: ChargebackScreenModel): ChargebackContentRobot {
        assertDisplayed(R.id.chargebackTitleLabel)
        assertDisplayed(R.id.chargebackContainer)
        assertRecyclerViewItemCount(R.id.reasonsView, expected.reasons.size)
        return this
    }

    fun leaveChargeback(): ChargebackContentRobot {
        click(R.id.leaveChargebackButton)
        return this
    }

    fun submitChargeback(): ChargebackContentRobot {
        click(R.id.performChargebackButton)
        return this
    }

    fun noInformationDisplayed(): ChargebackContentRobot {
        assertNotDisplayed(R.id.chargebackTitleLabel)
        assertNotDisplayed(R.id.chargebackContainer)
        return this
    }

}