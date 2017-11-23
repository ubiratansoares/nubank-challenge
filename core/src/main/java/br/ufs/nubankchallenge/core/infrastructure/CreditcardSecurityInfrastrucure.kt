package br.ufs.nubankchallenge.core.infrastructure

import br.ufs.nubankchallenge.core.domain.chargeback.CreditCardSecurity
import br.ufs.nubankchallenge.core.infrastructure.errorhandlers.InfraErrorsHandler
import br.ufs.nubankchallenge.core.infrastructure.rest.NubankWebService
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by @ubiratanfsoares
 *
 */
class CreditcardSecurityInfrastrucure(
        private val webService: NubankWebService,
        private val ioScheduler: Scheduler = Schedulers.trampoline()) : CreditCardSecurity {


    override fun blockSolicitation() =
            webService.blockCard()
                    .subscribeOn(ioScheduler)
                    .compose(InfraErrorsHandler())
                    .map { Unit }

    override fun unblockSoliction() = webService.unblockCard()
            .subscribeOn(ioScheduler)
            .compose(InfraErrorsHandler())
            .map { Unit }

}