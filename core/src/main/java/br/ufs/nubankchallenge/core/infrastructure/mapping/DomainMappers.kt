package br.ufs.nubankchallenge.core.infrastructure.mapping

import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackOptions
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackReclaim
import br.ufs.nubankchallenge.core.domain.chargeback.models.PossibleReclaimReason
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import br.ufs.nubankchallenge.core.domain.notice.ChargebackNotice
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackActionsPayload
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackNoticePayload
import br.ufs.nubankchallenge.core.infrastructure.models.SubmitChargebackBody
import br.ufs.nubankchallenge.core.infrastructure.models.SubmitChargebackBody.ReasonDetailBody

/**
 *
 * Created by @ubiratanfsoares
 *
 */

fun chargebackNoticeFromPayload(payload: ChargebackNoticePayload): ChargebackNotice {

    return with(payload) {
        ChargebackNotice(
                title = title ?: throw UndesiredResponse,
                rawDescription = description ?: throw UndesiredResponse,
                primaryActionText = primaryAction?.title ?: throw UndesiredResponse,
                secondaryActionText = secondaryAction?.title ?: throw UndesiredResponse)
    }
}

fun charegebackOptionsFromPayload(payload: ChargebackActionsPayload): ChargebackOptions {

    return with(payload) {
        val causes = reasons
                ?.map { PossibleReclaimReason(it.id!!, it.title!!) }
                ?: throw UndesiredResponse

        ChargebackOptions(
                disclaimer = title ?: throw UndesiredResponse,
                possibleReasons = causes,
                rawCommentHint = hint ?: throw UndesiredResponse,
                shouldBlockCreditcard = autoblock)
    }
}

fun chargebackReclaimToBody(reclaim: ChargebackReclaim): SubmitChargebackBody {

    return with(reclaim) {
        SubmitChargebackBody().apply {
            comment = userHistory
            details = frauds.map { ReasonDetailBody(it.id, it.choosed) }
        }
    }
}