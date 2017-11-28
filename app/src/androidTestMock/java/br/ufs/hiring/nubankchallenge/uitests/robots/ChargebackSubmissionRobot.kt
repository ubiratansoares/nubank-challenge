package br.ufs.hiring.nubankchallenge.uitests.robots

import android.app.Activity
import br.ufs.hiring.nubankchallenge.R
import com.schibsted.spain.barista.BaristaAssertions.assertDisplayed
import com.schibsted.spain.barista.BaristaClickActions.click
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

/**
 *
 * Created by @ubiratanfsoares
 *
 */


class ChargebackSubmissionRobot {

    fun checkSuccess(): ChargebackSubmissionRobot {
        assertDisplayed(R.string.title_chargeback_submission_success)
        assertDisplayed(R.string.message_submission_success)
        return this
    }

    fun internetErrorReported(): ChargebackSubmissionRobot {
        assertDisplayed(R.string.title_chargeback_submission_error)
        assertDisplayed(R.string.toast_internet_error)
        return this
    }

    fun infrastructureErrorReported(): ChargebackSubmissionRobot {
        assertDisplayed(R.string.title_chargeback_submission_error)
        assertDisplayed(R.string.toast_error_at_operation)
        return this
    }

    fun closesScreenWhenDone(target: Activity): ChargebackSubmissionRobot {
        click(R.id.submissionDone)
        assertTrue(target.isFinishing)
        return this
    }

    fun doesNotCloseScreenWhenDone(target: Activity): ChargebackSubmissionRobot {
        click(R.id.submissionDone)
        assertFalse(target.isFinishing)
        return this
    }


}