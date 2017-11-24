package br.ufs.hiring.nubankchallenge.factories

import br.ufs.nubankchallenge.core.infrastructure.ChargebackInfrastructure
import br.ufs.nubankchallenge.core.infrastructure.CreditcardSecurityInfrastrucure
import br.ufs.nubankchallenge.core.infrastructure.NoticeInfrastructure
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by @ubiratanfsoares
 *
 */

object InfrastructureFactory {

    private val worker = Schedulers.io()
    private val api = WebServiceFactory.webservice

    fun notice() = NoticeInfrastructure(api, worker)

    fun chargeback() = ChargebackInfrastructure(api, worker)

    fun cardBlocker() = CreditcardSecurityInfrastrucure(api, worker)

}