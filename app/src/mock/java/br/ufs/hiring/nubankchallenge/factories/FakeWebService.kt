package br.ufs.hiring.nubankchallenge.factories

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.RemoteSystemDown
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue.InternetUnreachable
import br.ufs.nubankchallenge.core.infrastructure.fromJson
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackActionsPayload
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackNoticePayload
import br.ufs.nubankchallenge.core.infrastructure.models.SubmitChargebackBody
import br.ufs.nubankchallenge.core.infrastructure.rest.NubankWebService
import com.google.gson.Gson
import io.reactivex.Observable

/**
 *
 * Created by @ubiratanfsoares
 *
 */

object FakeWebService : NubankWebService {

    private val gson by lazy { Gson() }

    private var actualScenario: TestScenario = TestScenario.NetworkingHeadache

    fun nextState(next: TestScenario) {
        actualScenario = next
    }

    override fun chargebackNotice(): Observable<ChargebackNoticePayload> =
            when (actualScenario) {
                is TestScenario.NetworkingHeadache -> someNetworkingError()
                is TestScenario.ServersDown -> serversDown()
                is TestScenario.ClientError -> clientError()
                is TestScenario.ChargebackNoticeRetrieved -> fakeNotice()
            }

    override fun chargebackActions(): Observable<ChargebackActionsPayload> {
        TODO("not implemented")
    }

    override fun blockCard(): Observable<Unit> {
        TODO("not implemented")
    }

    override fun unblockCard(): Observable<Unit> {
        TODO("not implemented")
    }

    override fun submitChargeback(body: SubmitChargebackBody): Observable<Unit> {
        TODO("not implemented")
    }


    private fun fakeNotice(): Observable<ChargebackNoticePayload> {
        val payload = gson.fromJson(chargebackNoticeResponse(), ChargebackNoticePayload::class)
        return Observable.just(payload)
    }

    private fun someNetworkingError(): Observable<ChargebackNoticePayload>
            = Observable.error(InternetUnreachable)

    private fun serversDown(): Observable<ChargebackNoticePayload>
            = Observable.error(RemoteSystemDown)

    private fun clientError(): Observable<ChargebackNoticePayload>
            = Observable.error(UndesiredResponse)

}