package br.ufs.nubankchallenge.core.tests.infrastructure

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue
import br.ufs.nubankchallenge.core.infrastructure.NoticeInfrastructure
import br.ufs.nubankchallenge.core.infrastructure.mapping.chargebackNoticeFromPayload
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackNoticePayload
import br.ufs.nubankchallenge.core.infrastructure.rest.NubankWebService
import br.ufs.nubankchallenge.core.tests.util.FileFromResources
import br.ufs.nubankchallenge.core.tests.util.Fixtures
import br.ufs.nubankchallenge.core.infrastructure.util.fromJson
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class NoticeInfrastructureTests {

    lateinit var infrastructure: NoticeInfrastructure
    lateinit var server: MockWebServer

    @Before fun `before each test`() {
        server = MockWebServer()
        val serverURL = server.url("/").toString()
        val webservice = Fixtures.nubankWebService(serverURL)
        infrastructure = NoticeInfrastructure(webservice)
    }

    @After fun `after each test`() {
        server.shutdown()
    }

    @Test fun `should integrate successfully with notice, exposing returned data`() {

        val json = FileFromResources("chargeback_notice_200OK.json")
        val payload = Gson().fromJson(json, ChargebackNoticePayload::class)
        val expected = chargebackNoticeFromPayload(payload)

        server.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .setBody(json)
        )

        infrastructure.execute()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue { it == expected }
    }

    @Test fun `should integrate handling error 4XY`() {

        server.enqueue(MockResponse().setResponseCode(400))

        infrastructure.execute()
                .test()
                .assertError { it == InfrastructureError.UndesiredResponse }
    }

    @Test fun `should integrate handling error 5XY`() {

        server.enqueue(MockResponse().setResponseCode(503))

        infrastructure.execute()
                .test()
                .assertError { it == InfrastructureError.RemoteSystemDown }
    }

    @Test fun `should integrate handling networking issue`() {

        val networkingError = Observable.error<ChargebackNoticePayload> {
            IOException("Canceled")
        }

        val mockedWebService = mock<NubankWebService> {
            on { chargebackNotice() } doReturn networkingError
        }

        NoticeInfrastructure(mockedWebService)
                .execute()
                .test()
                .assertError { it == NetworkingIssue.ConnectionSpike }
    }

    @Test fun `should integrate handling broken response`() {

        val json = FileFromResources("chargeback_notice_broken_200OK.json")

        server.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .setBody(json)
        )

        infrastructure.execute()
                .test()
                .assertError { it == InfrastructureError.UndesiredResponse }
    }

    @Test fun `should integrate handling missing mandatory field`() {

        val json = FileFromResources("chargeback_notice_missing_200OK.json")

        server.enqueue(
                MockResponse()
                        .setResponseCode(200)
                        .setBody(json)
        )

        infrastructure.execute()
                .test()
                .assertError { it == InfrastructureError.UndesiredResponse }
    }
}