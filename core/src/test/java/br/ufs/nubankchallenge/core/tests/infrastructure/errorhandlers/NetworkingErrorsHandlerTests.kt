package br.ufs.nubankchallenge.core.tests.infrastructure.errorhandlers

import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue.*
import br.ufs.nubankchallenge.core.infrastructure.errorhandlers.NetworkingErrorsHandler
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 *
 * Created by @ubiratanfsoares
 *
 */
class NetworkingErrorsHandlerTests {

    lateinit var handler: NetworkingErrorsHandler

    @Before fun `before each test`() {
        handler = NetworkingErrorsHandler()
    }

    @Test fun `should handle internet unavailable`() {
        val noInternet = Observable.error<Any>(UnknownHostException("No Internet"))
        noInternet.compose(handler)
                .test()
                .assertError(InternetUnreachable::class.java)
    }

    @Test fun `should handle connection errors`() {
        val brokenConnection = Observable.error<Any>(IOException("Canceled"))
        brokenConnection.compose(handler)
                .test()
                .assertError(ConnectionSpike::class.java)
    }

    @Test fun `should handle timeouts`() {
        val timeout = Observable.error<Any>(SocketTimeoutException())
        timeout.compose(handler)
                .test()
                .assertError(OperationTimeout::class.java)
    }

    @Test fun `should not handle other errors`() {
        val otherError = Observable.error<Any>(IllegalStateException("Broken json"))
        otherError.compose(handler)
                .test()
                .assertError({ throwable -> throwable !is NetworkingIssue })
    }
}