package br.ufs.nubankchallenge.core.tests.presentation

import br.ufs.nubankchallenge.core.domain.InfrastructureError
import br.ufs.nubankchallenge.core.domain.NetworkingIssue
import br.ufs.nubankchallenge.core.presentation.networking.NetworkingErrorFeedback
import br.ufs.nubankchallenge.core.presentation.networking.NetworkingErrorView
import br.ufs.nubankchallenge.core.tests.SilentObserver
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.atMost

/**
 *
 * Created by @ubiratanfsoares
 *
 */
class NetworingErrorFeedbackTests {

    val uiScheduler = Schedulers.trampoline()

    lateinit var networkingFeedback: NetworkingErrorFeedback
    lateinit var report: Action

    @Before fun `before each test`() {
        report = mock()

        val view = object : NetworkingErrorView {
            override fun reportNetworkingError(issue: NetworkingIssue): Action {
                return report
            }
        }

        networkingFeedback = NetworkingErrorFeedback(view, uiScheduler)
    }

    @Test fun `should not report networking error when flow emmits`() {
        Observable.just("A", "B", "C")
                .compose(networkingFeedback)
                .subscribe(SilentObserver)

        verify(report, never()).run()
    }

    @Test fun `should not report networking error when flow is empty`() {
        Observable.empty<Any>()
                .compose(networkingFeedback)
                .subscribe()

        verify(report, never()).run()
    }

    @Test fun `should not report networking error when upstream reports other errors`() {
        val otherError = InfrastructureError.RemoteSystemDown

        Observable.error<Any>(otherError)
                .compose(networkingFeedback)
                .subscribe(SilentObserver)

        verify(report, never()).run()
    }

    @Test fun `should report networking error when upstream reports`() {
        val networkingError = NetworkingIssue.ConnectionSpike

        Observable.error<Any>(networkingError)
                .compose(networkingFeedback)
                .subscribe(SilentObserver)

        verify(report, atMost(1)).run()
    }
}