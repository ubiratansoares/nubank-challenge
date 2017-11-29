package br.ufs.hiring.nubankchallenge.mocked

import br.ufs.hiring.nubankchallenge.factories.WebServiceFactory
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.RemoteSystemDown
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue.InternetUnreachable
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackActionsPayload
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackNoticePayload
import br.ufs.nubankchallenge.core.infrastructure.models.OperationResultPayload
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable

/**
 *
 * Created by @ubiratanfsoares
 *
 */

fun webServiceSimulatedWith(func: ScenarioSimulator.() -> Unit) {
    ScenarioSimulator.apply { func() }
}

object ScenarioSimulator {

    private val mockWebService by lazy { WebServiceFactory.webservice }

    fun serversProbablyDownWhenRetrievingNotice() {
        val scenario = Observable.error<ChargebackNoticePayload>(RemoteSystemDown)
        whenever(mockWebService.chargebackNotice()).thenReturn(scenario)
    }

    fun serversProbablyDownWhenRetrievingChargeback() {
        val scenario = Observable.error<ChargebackActionsPayload>(RemoteSystemDown)
        whenever(mockWebService.chargebackActions()).thenReturn(scenario)
    }

    fun undesiredResponseWhenRetrievingNotice() {
        val scenario = Observable.error<ChargebackNoticePayload>(UndesiredResponse)
        whenever(mockWebService.chargebackNotice()).thenReturn(scenario)
    }

    fun undesiredResponseWhenRetrievingChargeback() {
        val scenario = Observable.error<ChargebackActionsPayload>(UndesiredResponse)
        whenever(mockWebService.chargebackActions()).thenReturn(scenario)
    }

    fun internetIssueWhenRetrievingChargeback() {
        val scenario = Observable.error<ChargebackActionsPayload>(InternetUnreachable)
        whenever(mockWebService.chargebackActions()).thenReturn(scenario)
    }

    fun internetIssueWhenRetrievingNotice() {
        val scenario = Observable.error<ChargebackNoticePayload>(InternetUnreachable)
        whenever(mockWebService.chargebackNotice()).thenReturn(scenario)
    }

    fun noticeRetrievedWithSuccess() {
        whenever(mockWebService.chargebackNotice()).thenReturn(FakeResponses.noticeRetrived())
    }

    fun chargebackRetrievedWithSuccess() {
        whenever(mockWebService.chargebackActions())
                .thenReturn(FakeResponses.chargebackActions())
    }

    fun chargebackRetrievedAndCreditcardBlockedPreventively() {
        creditcardBlocksWithSuccess()
        whenever(mockWebService.chargebackActions())
                .thenReturn(FakeResponses.chargebackActionsWithPreventiveBlocking())
    }

    fun creditcardBlocksWithSuccess() {
        whenever(mockWebService.blockCard()).thenReturn(FakeResponses.operationSuccess())
    }

    fun creditcardUnblocksWithSuccess() {
        whenever(mockWebService.unblockCard()).thenReturn(FakeResponses.operationSuccess())
    }

    fun purchaseReclaimedWithSuccess() {
        whenever(mockWebService.submitChargeback(any()))
                .thenReturn(FakeResponses.operationSuccess())
    }

    fun purchaseReclaimFailsWithInternetError() {
        val scenario = Observable.error<OperationResultPayload>(InternetUnreachable)
        whenever(mockWebService.submitChargeback(any()))
                .thenReturn(scenario)
    }

    fun purchaseReclaimFailsWithInfrastructureError() {
        val scenario = Observable.error<OperationResultPayload>(RemoteSystemDown)
        whenever(mockWebService.submitChargeback(any()))
                .thenReturn(scenario)
    }

    fun creditcardBlocksFailsWithInternetError() {
        val scenario = Observable.error<OperationResultPayload>(InternetUnreachable)
        whenever(mockWebService.blockCard()).thenReturn(scenario)
    }

    fun creditcardUnblocksFailsWithInfrastructureError() {
        val scenario = Observable.error<OperationResultPayload>(RemoteSystemDown)
        whenever(mockWebService.unblockCard()).thenReturn(scenario)
    }


    fun allResponsesSucceed() {
        noticeRetrievedWithSuccess()
        chargebackRetrievedWithSuccess()
        creditcardBlocksWithSuccess()
        creditcardUnblocksWithSuccess()
        purchaseReclaimedWithSuccess()
    }
}