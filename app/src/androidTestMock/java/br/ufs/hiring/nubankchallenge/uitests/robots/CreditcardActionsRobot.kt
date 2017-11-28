package br.ufs.hiring.nubankchallenge.uitests.robots

import android.util.Log
import br.ufs.hiring.nubankchallenge.R
import br.ufs.nubankchallenge.core.presentation.chargeback.CreditcardState
import com.schibsted.spain.barista.BaristaAssertions.assertDisplayed
import com.schibsted.spain.barista.BaristaClickActions.click

/**
 *
 * Created by @ubiratanfsoares
 *
 */

fun creditcard(func: CreditcardActionsRobot.() -> Unit) =
        CreditcardActionsRobot().apply { func() }

class CreditcardActionsRobot {

    fun performCreditcardBlocking() {
        click(R.id.lockpadView)
    }

    fun performCreditcardUnblocking() {
        click(R.id.lockpadView)
    }

    fun verifyCreditcardAs(expected: CreditcardState) {
        Log.v("Robot", expected.toString())
        assertDisplayed(expected.disclaimerResource)
    }
}

