package br.ufs.hiring.nubankchallenge.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import br.ufs.hiring.nubankchallenge.R
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.RemoteSystemDown
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue
import kotlinx.android.synthetic.main.view_error_feedback.view.*

/**
 *
 * Created by @ubiratanfsoares
 *
 */
class ErrorStateContainer @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var feedback: Feedback

    fun setState(error: Throwable) {

        when (error) {

            is UndesiredResponse -> {
                feedback = Feedback(
                        R.drawable.img_undesired_response,
                        R.string.error_undesired_response
                )
            }

            is RemoteSystemDown -> {
                feedback = Feedback(
                        R.drawable.img_servers_down,
                        R.string.error_server_down
                )
            }

            is NetworkingIssue -> {
                feedback = Feedback(
                        R.drawable.img_network_offline,
                        R.string.error_internet_connection
                )
            }
        }

        showFeeback()
    }

    private fun showFeeback() {
        errorImage.setImageResource(feedback.image)
        errorMessage.text = resources.getString(feedback.message)
    }

    private data class Feedback(val image: Int, val message: Int)

    fun reset() {
        feedback = Feedback(0, 0)
        errorImage.setImageResource(0)
        errorMessage.text = ""
    }
}