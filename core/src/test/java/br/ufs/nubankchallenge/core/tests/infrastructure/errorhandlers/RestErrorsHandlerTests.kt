package br.ufs.nubankchallenge.core.tests.infrastructure.errorhandlers

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.RemoteSystemDown
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import br.ufs.nubankchallenge.core.infrastructure.errorhandlers.RestErrorsHandler
import br.ufs.nubankchallenge.core.tests.util.Fixtures
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

/**
 *
 * Created by @ubiratanfsoares
 *
 */
class RestErrorsHandlerTests {

    lateinit var handler: RestErrorsHandler

    @Before fun `before each test`() {
        handler = RestErrorsHandler()
    }

    @Test fun `should handle internal server error`() {
        val responseBody = "{\"message\":\"Internal server error\"}"
        val error = Fixtures.httpError(503, responseBody)
        val internalServer: Observable<String> = Observable.error(error)

        internalServer.compose(handler)
                .test()
                .assertError(RemoteSystemDown::class.java)
    }

    @Test fun `should handle undesired returns`() {
        val responseBody = "{\"message\":\"You failed\"}"
        val error = Fixtures.httpError(400, responseBody)
        val badRequest: Observable<String> = Observable.error(error)

        badRequest.compose(handler)
                .test()
                .assertError(UndesiredResponse::class.java)
    }

    @Test fun `should not handle any other errros`() {

        val npe = NullPointerException()
        val nonRestIssue = Observable.error<Any>(npe)

        nonRestIssue.compose(handler)
                .test()
                .assertError { throwable -> throwable == npe }
    }

}