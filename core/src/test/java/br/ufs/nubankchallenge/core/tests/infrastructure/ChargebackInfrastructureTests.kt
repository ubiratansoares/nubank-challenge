package br.ufs.nubankchallenge.core.tests.infrastructure

import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackReclaim
import br.ufs.nubankchallenge.core.domain.chargeback.models.Fraud
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.RemoteSystemDown
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue.OperationTimeout
import br.ufs.nubankchallenge.core.infrastructure.ChargebackInfrastructure
import br.ufs.nubankchallenge.core.infrastructure.mapping.charegebackOptionsFromPayload
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackActionsPayload
import br.ufs.nubankchallenge.core.infrastructure.rest.NubankWebService
import br.ufs.nubankchallenge.core.infrastructure.util.fromJson
import br.ufs.nubankchallenge.core.tests.util.FileFromResources
import br.ufs.nubankchallenge.core.tests.util.Fixtures
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.SocketTimeoutException

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class ChargebackInfrastructureTests {

    lateinit var infrastructure: ChargebackInfrastructure
    lateinit var server: MockWebServer

    @Before fun `before each test`() {
        server = MockWebServer()
        val serverURL = server.url("/").toString()
        val webservice = Fixtures.nubankWebService(serverURL)
        infrastructure = ChargebackInfrastructure(webservice)
    }

    @After fun `after each test`() {
        server.shutdown()
    }

    @Test fun `should integrate handling error 4XY`() {

        server.enqueue(MockResponse().setResponseCode(418))

        infrastructure.possibleActions()
                .test()
                .assertError { it == UndesiredResponse }
    }

    @Test fun `should integrate handling error 5XY`() {

        server.enqueue(MockResponse().setResponseCode(502))

        infrastructure.possibleActions()
                .test()
                .assertError { it == RemoteSystemDown }
    }

    @Test fun `should integrate handling networking issue`() {

        val networkingError = Observable.error<ChargebackActionsPayload> {
            SocketTimeoutException("Timeout")
        }

        val mockedWebService = mock<NubankWebService> {
            on { chargebackActions() } doReturn networkingError
        }

        ChargebackInfrastructure(mockedWebService)
                .possibleActions()
                .test()
                .assertError { it == OperationTimeout }
    }

    @Test fun `should integrate handling broken response`() {

        val json = FileFromResources("chargeback_actions_broken_200OK.json")

        server.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .setBody(json)
        )

        infrastructure.possibleActions()
                .test()
                .assertError { it == InfrastructureError.UndesiredResponse }
    }

    @Test fun `should integrate handling missing mandatory field`() {

        val json = FileFromResources("chargeback_actions_missing_200OK.json")

        server.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .setBody(json)
        )

        infrastructure.possibleActions()
                .test()
                .assertError { it == InfrastructureError.UndesiredResponse }
    }

    @Test fun `should integrate retrieving charback options`() {

        val json = FileFromResources("chargeback_actions_200OK.json")

        val payload = Gson().fromJson(json, ChargebackActionsPayload::class)
        val expected = charegebackOptionsFromPayload(payload)

        server.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .setBody(json)
        )

        infrastructure.possibleActions()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue { it == expected }
    }

    @Test fun `should integrate posting new chargeback reclaim`() {

        val reclaim = ChargebackReclaim(
                userHistory = "Não fui eu!!!!",
                frauds = listOf(Fraud("merchant_recognized", true))
        )

        server.enqueue(
                MockResponse()
                        .setResponseCode(201)
                        .setBody("{\"status\": \"Ok\"}")
        )

        infrastructure.withReclaim(reclaim)
                .test()
                .assertNoErrors()
                .assertComplete()
    }
}