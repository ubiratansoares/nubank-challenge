package br.ufs.hiring.nubankchallenge.uitests.robots

import br.ufs.hiring.nubankchallenge.R
import com.schibsted.spain.barista.BaristaAssertions.*

/**
 *
 * Created by @ubiratanfsoares
 *
 */

fun errorFeedback(func: ErrorFeebackVerifier.() -> Unit)
        = ErrorFeebackVerifier().apply { func() }

class ErrorFeebackVerifier {

    fun serverErrorReported(): ErrorFeebackVerifier {
        assertDrawable(R.id.errorImage, R.drawable.img_servers_down)
        assertDisplayed(R.string.error_server_down)
        assertDisplayed(R.string.snacktext_servers_down)
        return this
    }

    fun internetIssueReported(): ErrorFeebackVerifier {
        assertDrawable(R.id.errorImage, R.drawable.img_network_offline)
        assertDisplayed(R.string.error_internet_connection)
        assertDisplayed(R.string.snacktext_internet_connection)
        return this
    }

    fun noErrorFeedbackGiven(): ErrorFeebackVerifier {
        assertNotDisplayed(R.id.feedbackContainer)
        assertNotExist(R.id.snackbar_text)
        return this
    }

    fun undesiredResponseReported(): ErrorFeebackVerifier {
        assertDrawable(R.id.errorImage, R.drawable.img_undesired_response)
        assertDisplayed(R.string.error_undesired_response)
        assertDisplayed(R.string.snacktext_undesired_response)
        return this
    }
}