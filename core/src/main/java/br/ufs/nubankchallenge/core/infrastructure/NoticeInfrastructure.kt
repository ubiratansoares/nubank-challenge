package br.ufs.nubankchallenge.core.infrastructure

import br.ufs.nubankchallenge.core.domain.notice.ChargebackNotice
import br.ufs.nubankchallenge.core.domain.notice.RetrieveNotice
import br.ufs.nubankchallenge.core.infrastructure.errorhandlers.InfraErrorsHandler
import br.ufs.nubankchallenge.core.infrastructure.mapping.chargebackNoticeFromPayload
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackNoticePayload
import br.ufs.nubankchallenge.core.infrastructure.rest.NubankWebService
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class NoticeInfrastructure(
        private val webService: NubankWebService,
        private val ioScheduler: Scheduler = Schedulers.trampoline()) : RetrieveNotice {

    override fun execute(): Observable<ChargebackNotice> {
        return webService
                .chargebackNotice()
                .subscribeOn(ioScheduler)
                .compose(InfraErrorsHandler())
                .map { chargebackNoticeFromPayload(it as ChargebackNoticePayload) }
    }
}