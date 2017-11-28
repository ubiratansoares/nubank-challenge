package br.ufs.hiring.nubankchallenge.uitests.robots

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.Window
import br.ufs.hiring.nubankchallenge.R
import br.ufs.nubankchallenge.core.presentation.chargeback.CreditcardState
import com.schibsted.spain.barista.BaristaAssertions.assertDisplayed
import com.schibsted.spain.barista.BaristaClickActions.click
import org.hamcrest.CoreMatchers.not


/**
 *
 * Created by @ubiratanfsoares
 *
 */

class CreditcardActionsRobot {

    fun performCreditcardBlocking() {
        click(R.id.lockpadView)
    }

    fun performCreditcardUnblocking() {
        click(R.id.lockpadView)
    }

    fun verifyCreditcardAs(expected: CreditcardState) {
        assertDisplayed(expected.disclaimerResource)
    }

    fun internetErrorReported(window: Window) {
        onView(withText(R.string.toast_internet_error))
                .inRoot(withDecorView(not(window.decorView))) // WTF Google
                .check(matches(isDisplayed()))
    }


    fun infrastructureErrorReported(window: Window) {
        onView(withText(R.string.toast_error_at_operation))
                .inRoot(withDecorView(not(window.decorView)))
                .check(matches(isDisplayed()))
    }
}

