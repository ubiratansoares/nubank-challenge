package br.ufs.nubankchallenge.core.tests.infrastructure

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue
import br.ufs.nubankchallenge.core.infrastructure.CreditcardSecurityInfrastrucure
import br.ufs.nubankchallenge.core.infrastructure.models.OperationResultPayload
import br.ufs.nubankchallenge.core.infrastructure.rest.NubankWebService
import br.ufs.nubankchallenge.core.tests.util.Fixtures
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

class CreditcardSecurityInfrastructureTests {

    lateinit var infrastructure: CreditcardSecurityInfrastrucure
    lateinit var server: MockWebServer

    @Before fun `before each test`() {
        server = MockWebServer()
        val serverURL = server.url("/").toString()
        val webservice = Fixtures.nubankWebService(serverURL)
        infrastructure = CreditcardSecurityInfrastrucure(webservice)
    }

    @After fun `after each test`() {
        server.shutdown()
    }

    @Test fun `should integrate handling error 4XY`() {

        server.enqueue(MockResponse().setResponseCode(418))

        infrastructure.blockSolicitation()
                .test()
                .assertError { it == InfrastructureError.UndesiredResponse }
    }

    @Test fun `should integrate handling error 5XY`() {

        server.enqueue(MockResponse().setResponseCode(502))

        infrastructure.unblockSoliction()
                .test()
                .assertError { it == InfrastructureError.RemoteSystemDown }
    }

    @Test fun `should integrate handling networking issue`() {

        val networkingError = Observable.error<OperationResultPayload> {
            SocketTimeoutException("Timeout")
        }

        val mockedWebService = mock<NubankWebService> {
            on { blockCard() } doReturn networkingError
        }

        CreditcardSecurityInfrastrucure(mockedWebService)
                .blockSolicitation()
                .test()
                .assertError { it == NetworkingIssue.OperationTimeout }
    }

    @Test fun `should integrate with success for creditcard operation`() {

        server.enqueue(
                MockResponse()
                        .setResponseCode(201)
                        .setBody("{\"status\": \"Ok\"}")
        )

        infrastructure.unblockSoliction()
                .test()
                .assertComplete()
                .assertNoErrors()
    }
}