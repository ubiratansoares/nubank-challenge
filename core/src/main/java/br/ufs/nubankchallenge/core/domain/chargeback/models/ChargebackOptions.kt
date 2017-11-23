package br.ufs.nubankchallenge.core.domain.chargeback.models

/**
 *
 * Created by @ubiratanfsoares
 *
 */

data class ChargebackOptions(
        val disclaimer: String,
        val rawCommentHint: String,
        val shouldBlockCreditcard: Boolean,
        val possibleReasons: List<PossibleReclaimReason>
)
