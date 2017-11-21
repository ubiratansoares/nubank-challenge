package br.ufs.nubankchallenge.core.infrastructure.mapping

import br.ufs.nubankchallenge.core.domain.chargeback.ChargebackNotice
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackNoticePayload

/**
 *
 * Created by @ubiratanfsoares
 *
 */

fun chargebackNoticeFromPayload(payload: ChargebackNoticePayload) : ChargebackNotice{
    return ChargebackNotice(
            title = payload.title,
            rawDescription = payload.description,
            primaryActionText = payload.primaryAction.title,
            secondaryActionText = payload.secondaryAction.title
    )
}