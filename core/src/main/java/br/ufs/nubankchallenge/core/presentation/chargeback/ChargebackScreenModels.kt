package br.ufs.nubankchallenge.core.presentation.chargeback

import android.text.Html
import android.text.Spanned
import br.ufs.nubankchallenge.core.R
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackOptions
import br.ufs.nubankchallenge.core.presentation.chargeback.LockpadState.*

/**
 *
 * Created by @ubiratanfsoares
 *
 */

data class ChargebackScreenModel(
        val screenTitle: String,
        val commentHint: Spanned,
        val lockpadState: LockpadState,
        val reasons: List<ReasonRowModel>) {

    companion object Mapper {
        operator fun invoke(
                options: ChargebackOptions,
                actualLockpadState: LockpadState): ChargebackScreenModel {

            return with(options) {
                ChargebackScreenModel(
                        screenTitle = disclaimer,
                        commentHint = Html.fromHtml(rawCommentHint),
                        lockpadState = updateLockpad(shouldBlockCreditcard, actualLockpadState),
                        reasons = possibleReasons.map { ReasonRowModel(it.id, it.title) }
                )
            }
        }

        private fun updateLockpad(shouldBlock: Boolean,
                                  actual: LockpadState): LockpadState {

            return when (actual) {
                is LockedByUser -> actual
                is UnlockedByUser -> actual
                else -> if (shouldBlock) return LockedBySystem else UnlockedByDefault
            }
        }
    }
}

sealed class LockpadState(val disclaimerResource: Int, val lockPadImage: Int) {
    object LockedBySystem :
            LockpadState(R.string.message_cardblocked, R.drawable.ic_chargeback_lock)

    object LockedByUser :
            LockpadState(R.string.message_cardblocked, R.drawable.ic_chargeback_lock)

    object UnlockedByDefault :
            LockpadState(R.string.message_cardunblocked, R.drawable.ic_chargeback_unlock)

    object UnlockedByUser :
            LockpadState(R.string.message_cardunblocked, R.drawable.ic_chargeback_unlock)
}


data class ReasonRowModel(
        val id: String,
        val description: String,
        var choosedByUser: Boolean = false
)