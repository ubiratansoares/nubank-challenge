package br.ufs.nubankchallenge.core.tests.infrastructure.errorhandlers

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue
import br.ufs.nubankchallenge.core.infrastructure.errorhandlers.InfraErrorsHandler
import br.ufs.nubankchallenge.core.tests.util.Fixtures
import com.google.gson.JsonSyntaxException
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

/**
 *
 * Created by @ubiratanfsoares
 *
 */
class InfraErrorsHandlerTests {

    lateinit var handler: InfraErrorsHandler

    @Before fun `before each test`() {
        handler = InfraErrorsHandler()
    }

    @Test fun `should catch networking errors`() {

        val noInternet = Observable.error<Any>(UnknownHostException("No Internet"))
        noInternet.compose(handler)
                .test()
                .assertError(NetworkingIssue.InternetUnreachable::class.java)
    }

    @Test fun `should catch rest errors`() {
        val responseBody = "{\"message\":\"Internal server error\"}"
        val error = Fixtures.httpError(503, responseBody)
        val internalServer: Observable<String> = Observable.error(error)

        internalServer.compose(handler)
                .test()
                .assertError(InfrastructureError.RemoteSystemDown::class.java)
    }

    @Test fun `should catch deserialization errors`() {
        val broken = Observable.error<Any>(JsonSyntaxException("Json should not have comments"))

        broken.compose(handler)
                .test()
                .assertError { error -> error is InfrastructureError.UndesiredResponse }
    }

}