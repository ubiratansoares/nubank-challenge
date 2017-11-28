package br.ufs.hiring.nubankchallenge.uitests.robots

import br.ufs.hiring.nubankchallenge.factories.FakeResponses
import br.ufs.hiring.nubankchallenge.factories.WebServiceFactory
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.RemoteSystemDown
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue.InternetUnreachable
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackActionsPayload
import br.ufs.nubankchallenge.core.infrastructure.models.OperationResultPayload
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable

/**
 *
 * Created by @ubiratanfsoares
 *
 */

fun webServiceSimulatedWith(func: ScenariosProvider.() -> Unit) {
    ScenariosProvider.apply { func() }
}

object ScenariosProvider {

    private val mockWebService by lazy { WebServiceFactory.webservice }

    fun serversProbablyDown() {
        val scenario = Observable.error<ChargebackActionsPayload>(RemoteSystemDown)
        whenever(mockWebService.chargebackActions()).thenReturn(scenario)
    }

    fun mysteriousClientError() {
        val scenario = Observable.error<ChargebackActionsPayload>(UndesiredResponse)
        whenever(mockWebService.chargebackActions()).thenReturn(scenario)
    }

    fun internetIssue() {
        val scenario = Observable.error<ChargebackActionsPayload>(InternetUnreachable)
        whenever(mockWebService.chargebackActions()).thenReturn(scenario)
    }

    fun chargebackRetrieved() {
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

    fun reclaimedWithSuccess() {
        whenever(mockWebService.submitChargeback(any()))
                .thenReturn(FakeResponses.operationSuccess())
    }

    fun reclaimFailsWithInternetError() {
        val scenario = Observable.error<OperationResultPayload>(InternetUnreachable)
        whenever(mockWebService.submitChargeback(any()))
                .thenReturn(scenario)
    }

    fun reclaimFailsWithInfrastructureError() {
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
}