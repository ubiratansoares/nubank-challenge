package br.ufs.nubankchallenge.core.presentation.notice

import android.text.Html
import android.text.Spanned
import br.ufs.nubankchallenge.core.domain.notice.ChargebackNotice

/**
 *
 * Created by @ubiratanfsoares
 *
 */

data class NoticeScreenModel(
        val title: String,
        val formattedDescription: Spanned,
        val proceedButtonLabel: String,
        val cancelButtonLabel: String) {


    override fun equals(other: Any?): Boolean {
        if (other is NoticeScreenModel) {

            // We are forced to do this since SpannableStringBuilder.hashcode() is broken ...
            return other.title == title &&
                    other.proceedButtonLabel == proceedButtonLabel &&
                    other.cancelButtonLabel == cancelButtonLabel &&
                    other.formattedDescription.toString() == formattedDescription.toString()
        }

        return super.equals(other)
    }

    companion object Mapper {
        operator fun invoke(notice: ChargebackNotice): NoticeScreenModel {
            return NoticeScreenModel(
                    title = notice.title,
                    formattedDescription = Html.fromHtml(notice.rawDescription),
                    proceedButtonLabel = notice.primaryActionText,
                    cancelButtonLabel = notice.secondaryActionText
            )
        }
    }

}

