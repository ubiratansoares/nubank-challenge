package br.ufs.nubankchallenge.core.presentation.chargeback

import android.text.Html
import android.text.Spanned
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackOptions

/**
 *
 * Created by @ubiratanfsoares
 *
 */

data class ChargebackScreenModel(
        val screenTitle: String,
        val commentHint: Spanned,
        val lockpadInfo: LockpadModel,
        val reasons: List<ReasonRowModel>,
        val cardBlockedByUser: Boolean = false) {

    companion object Mapper {
        operator fun invoke(
                options: ChargebackOptions,
                alreadyBlocked: Boolean = false): ChargebackScreenModel {

            return with(options) {
                ChargebackScreenModel(
                        screenTitle = disclaimer,
                        commentHint = Html.fromHtml(rawCommentHint),
                        lockpadInfo = buildLockpadModel(shouldBlockCreditcard, alreadyBlocked),
                        reasons = possibleReasons.map { ReasonRowModel(it.id, it.title) },
                        cardBlockedByUser = alreadyBlocked
                )
            }
        }

        private fun buildLockpadModel(shouldBlock: Boolean,
                                      alreadyBlocked: Boolean): LockpadModel {

            if (alreadyBlocked || shouldBlock) {
                return LockpadModel(0, 0)
            }

            return LockpadModel(1, 1)
        }
    }
}

data class LockpadModel(
        val disclaimerResource: Int,
        val lockPadImage: Int
)

data class ReasonRowModel(
        val id: String,
        val description: String,
        val choosedByUser: Boolean = false
)