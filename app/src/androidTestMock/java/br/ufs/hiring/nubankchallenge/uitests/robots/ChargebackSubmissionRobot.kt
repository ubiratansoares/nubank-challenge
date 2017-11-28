package br.ufs.hiring.nubankchallenge.uitests.robots

import android.app.Activity
import br.ufs.hiring.nubankchallenge.R
import com.schibsted.spain.barista.BaristaAssertions.assertDisplayed
import com.schibsted.spain.barista.BaristaClickActions.click
import org.junit.Assert

/**
 *
 * Created by @ubiratanfsoares
 *
 */

fun chargebackSubmission(func: ChargebackSubmissionRobot.() -> Unit) =
        ChargebackSubmissionRobot().apply { func() }

class ChargebackSubmissionRobot {

    fun checkSuccess(): ChargebackSubmissionRobot {
        assertDisplayed(R.string.title_chargeback_submission_success)
        assertDisplayed(R.string.message_submission_success)
        return this
    }

    infix fun closesScreenWhenDone(target: Activity): ChargebackSubmissionRobot {
        click(R.id.submissionDone)
        Assert.assertTrue(target.isFinishing)
        return this
    }

    infix fun doesNotCloseScreen(target: Activity): ChargebackSubmissionRobot {
        click(R.id.submissionDone)
        Assert.assertFalse(target.isFinishing)
        return this
    }
}