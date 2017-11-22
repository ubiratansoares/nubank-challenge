package br.ufs.nubankchallenge.core.tests.presentation.behaviors

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue
import br.ufs.nubankchallenge.core.presentation.behaviors.BehaviorsPresenter
import br.ufs.nubankchallenge.core.presentation.errorstate.ErrorStateView
import br.ufs.nubankchallenge.core.presentation.loading.LoadingView
import br.ufs.nubankchallenge.core.presentation.networking.NetworkingErrorView
import br.ufs.nubankchallenge.core.tests.util.SilentObserver
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class BehaviorsPresenterTests {

    private val uiScheduler = Schedulers.trampoline()

    lateinit var showErrorState: Action
    lateinit var hideErrorState: Action
    lateinit var reportNetworkingError: Action
    lateinit var showLoading: Action
    lateinit var hideLoading: Action

    lateinit var presenter: BehaviorsPresenter

    @Before fun `before each test`() {
        showErrorState = mock()
        hideErrorState = mock()
        reportNetworkingError = mock()
        showLoading = mock()
        hideLoading = mock()

        val view = object : ErrorStateView, NetworkingErrorView, LoadingView {
            override fun showLoading(): Action {
                return showLoading
            }

            override fun hideLoading(): Action {
                return hideLoading
            }

            override fun reportNetworkingError(issue: NetworkingIssue): Action {
                return reportNetworkingError
            }

            override fun showErrorState(error: InfrastructureError): Action {
                return showErrorState
            }

            override fun hideErrorState(): Action {
                return hideErrorState
            }
        }

        presenter = BehaviorsPresenter(view, uiScheduler)
    }

    @Test fun `should integrate multiple behaviors, infrastructure error`() {
        Observable.error<Any>(InfrastructureError.RemoteSystemDown)
                .compose(presenter)
                .subscribe(SilentObserver)

        verify(showErrorState).run()
        verify(reportNetworkingError, never()).run()

        `verify loading actions`()
    }

    @Test fun `should integrate multiple behaviors, networking error`() {
        Observable.error<Any>(NetworkingIssue.ConnectionSpike)
                .compose(presenter)
                .subscribe(SilentObserver)

        verify(reportNetworkingError).run()
        verify(showErrorState, never()).run()

        `verify loading actions`()
    }

    private fun `verify loading actions`() {
        inOrder(showLoading, hideLoading) {
            verify(showLoading).run()
            verify(hideLoading).run()
        }
    }
}