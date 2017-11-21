package br.ufs.nubankchallenge.core.tests.infrastructure

import br.ufs.nubankchallenge.core.infrastructure.NoticeInfrastructure
import br.ufs.nubankchallenge.core.infrastructure.mapping.chargebackNoticeFromPayload
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackNoticePayload
import br.ufs.nubankchallenge.core.tests.util.FileFromResources
import br.ufs.nubankchallenge.core.tests.util.Fixtures
import br.ufs.nubankchallenge.core.tests.util.fromJson
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

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
}