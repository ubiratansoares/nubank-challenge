package br.ufs.nubankchallenge.core.tests.infrastructure

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.RemoteSystemDown
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import br.ufs.nubankchallenge.core.infrastructure.RestErrorsHandler
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

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
        val error = newHttpError(503, responseBody)
        val internalServer: Observable<String> = Observable.error(error)

        internalServer.compose(handler)
                .test()
                .assertError(RemoteSystemDown::class.java)
    }

    @Test fun `should handle undesired returns`() {
        val responseBody = "{\"message\":\"You failed\"}"
        val error = newHttpError(400, responseBody)
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

    private fun newHttpError(statusCode: Int, errorMessage: String): HttpException {
        val jsonMediaType = MediaType.parse("application/json")
        val body = ResponseBody.create(jsonMediaType, errorMessage)
        val response: Response<String> = Response.error(statusCode, body)
        return HttpException(response)
    }
}