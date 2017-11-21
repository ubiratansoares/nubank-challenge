package br.ufs.nubankchallenge.core.infrastructure.mapping

import br.ufs.nubankchallenge.core.domain.chargeback.ChargebackNotice
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackNoticePayload

/**
 *
 * Created by @ubiratanfsoares
 *
 */

fun chargebackNoticeFromPayload(payload: ChargebackNoticePayload): ChargebackNotice {
    return ChargebackNotice(
            title = payload.title ?: throw UndesiredResponse,
            rawDescription = payload.description ?: throw UndesiredResponse,
            primaryActionText = payload.primaryAction?.title ?: throw UndesiredResponse,
            secondaryActionText = payload.secondaryAction?.title ?: throw UndesiredResponse
    )
}