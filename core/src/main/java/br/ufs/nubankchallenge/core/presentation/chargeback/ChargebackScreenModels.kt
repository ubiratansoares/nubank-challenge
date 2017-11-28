package br.ufs.nubankchallenge.core.presentation.chargeback

import android.text.Html
import android.text.Spanned
import br.ufs.nubankchallenge.core.R
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackOptions
import br.ufs.nubankchallenge.core.presentation.chargeback.CreditcardState.*

/**
 *
 * Created by @ubiratanfsoares
 *
 */

data class ChargebackScreenModel(
        val screenTitle: String,
        val commentHint: Spanned,
        val creditcardState: CreditcardState,
        val reasons: List<ReasonRowModel>) {

    companion object Mapper {
        operator fun invoke(
                options: ChargebackOptions,
                actualCreditcardState: CreditcardState): ChargebackScreenModel {

            return with(options) {
                ChargebackScreenModel(
                        screenTitle = disclaimer,
                        commentHint = Html.fromHtml(rawCommentHint),
                        creditcardState = updateLockpad(shouldBlockCreditcard, actualCreditcardState),
                        reasons = possibleReasons.map { ReasonRowModel(it.id, it.title) }
                )
            }
        }

        private fun updateLockpad(shouldBlock: Boolean,
                                  actual: CreditcardState): CreditcardState {

            return when (actual) {
                is LockedByUser -> actual
                is UnlockedByUser -> actual
                else -> if (shouldBlock) return LockedBySystem else UnlockedByDefault
            }
        }
    }
}

sealed class CreditcardState(val disclaimerResource: Int, val lockPadImage: Int) {
    object LockedBySystem :
            CreditcardState(R.string.message_cardblocked, R.drawable.ic_chargeback_lock)

    object LockedByUser :
            CreditcardState(R.string.message_cardblocked, R.drawable.ic_chargeback_lock)

    object UnlockedByDefault :
            CreditcardState(R.string.message_cardunblocked, R.drawable.ic_chargeback_unlock)

    object UnlockedByUser :
            CreditcardState(R.string.message_cardunblocked, R.drawable.ic_chargeback_unlock)
}


data class ReasonRowModel(
        val id: String,
        val description: String,
        var choosedByUser: Boolean = false
)