package br.ufs.nubankchallenge.core.presentation.chargeback

import android.text.Html
import android.text.Spanned
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
                                  actualLockpadState: LockpadState): LockpadState {

            return when (actualLockpadState) {
                is LockedByUser -> actualLockpadState
                is UnlockedByUser -> actualLockpadState
                else -> if (shouldBlock) return LockedBySystem else UnlockedByDefault
            }
        }
    }
}

sealed class LockpadState(val disclaimerResource: Int, val lockPadImage: Int) {
    object LockedBySystem : LockpadState(0, 0)
    object LockedByUser : LockpadState(0, 0)
    object UnlockedByDefault : LockpadState(1, 1)
    object UnlockedByUser : LockpadState(1, 1)
}

data class ReasonRowModel(
        val id: String,
        val description: String,
        val choosedByUser: Boolean = false
)