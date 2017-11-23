package br.ufs.hiring.nubankchallenge.uitests.robots

import br.ufs.hiring.nubankchallenge.R
import com.schibsted.spain.barista.BaristaAssertions.assertDisplayed
import com.schibsted.spain.barista.BaristaAssertions.assertNotDisplayed
import com.schibsted.spain.barista.BaristaClickActions.click

/**
 *
 * Created by @ubiratanfsoares
 *
 */

fun noticeInformation(func: NoticeVerifier.() -> Unit) = NoticeVerifier().apply { func() }

class NoticeVerifier {

    fun loadingIndicatorHidden(): NoticeVerifier {
        assertNotDisplayed(R.id.loadingIndicator)
        return this
    }

    fun actionButtonsUnavailable(): NoticeVerifier {
        assertNotDisplayed(R.id.buttonsContainer)
        return this
    }

    fun actionButtonsAvailable(): NoticeVerifier {
        assertDisplayed(R.id.buttonsContainer)
        return this
    }

    fun noticeDelivered(): NoticeVerifier {
        assertDisplayed(R.id.noticeTitleLabel)
        assertDisplayed(R.id.noticeDescriptionLabel)
        return this
    }

    fun noticeTitle(title: String): NoticeVerifier {
        assertDisplayed(title)
        return this
    }

    fun primaryActionButton(name: String): NoticeVerifier {
        assertDisplayed(name)
        return this
    }

    fun secondaryActionButton(name: String): NoticeVerifier {
        assertDisplayed(name)
        return this
    }

    fun noInformationDisplayed(): NoticeVerifier {
        assertNotDisplayed(R.id.noticeTitleLabel)
        assertNotDisplayed(R.id.noticeDescriptionLabel)
        return this
    }

    fun retry(): NoticeVerifier {
        click(R.string.snackaction_retry)
        return this;
    }

    fun userGivesUpOnChargeback() {
        click(R.id.cancelButton)
    }
}