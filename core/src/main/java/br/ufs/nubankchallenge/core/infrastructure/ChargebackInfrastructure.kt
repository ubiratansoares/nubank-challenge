package br.ufs.nubankchallenge.core.infrastructure

import br.ufs.nubankchallenge.core.domain.chargeback.RetrieveChargebackOptions
import br.ufs.nubankchallenge.core.domain.chargeback.SubmitNewChargeback
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackOptions
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackReclaim
import br.ufs.nubankchallenge.core.infrastructure.errorhandlers.InfraErrorsHandler
import br.ufs.nubankchallenge.core.infrastructure.mapping.charegebackOptionsFromPayload
import br.ufs.nubankchallenge.core.infrastructure.mapping.chargebackReclaimToBody
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackActionsPayload
import br.ufs.nubankchallenge.core.infrastructure.rest.NubankWebService
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class ChargebackInfrastructure(
        private val webService: NubankWebService,
        private val ioScheduler: Scheduler = Schedulers.trampoline()
) : RetrieveChargebackOptions, SubmitNewChargeback {

    override fun withReclaim(reclaim: ChargebackReclaim): Observable<Unit> {
        return webService.submitChargeback(chargebackReclaimToBody(reclaim))
                .subscribeOn(ioScheduler)
                .compose(InfraErrorsHandler())
                .map { Unit }
    }

    override fun possibleActions(): Observable<ChargebackOptions> {
        return webService.chargebackActions()
                .subscribeOn(ioScheduler)
                .compose(InfraErrorsHandler())
                .map { charegebackOptionsFromPayload(it as ChargebackActionsPayload) }
    }

}