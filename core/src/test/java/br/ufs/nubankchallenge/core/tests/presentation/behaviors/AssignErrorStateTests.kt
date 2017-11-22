package br.ufs.nubankchallenge.core.tests.presentation.behaviors

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.presentation.errorstate.AssignErrorState
import br.ufs.nubankchallenge.core.presentation.errorstate.ErrorStateView
import br.ufs.nubankchallenge.core.tests.util.SilentObserver
import com.nhaarman.mockito_kotlin.atLeastOnce
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
class AssignErrorStateTests {
    val uiScheduler = Schedulers.trampoline()
    lateinit var errorStateAssigner: AssignErrorState

    lateinit var showAction: Action
    lateinit var hideAction: Action

    @Before fun `before each test`() {
        showAction = mock()
        hideAction = mock()

        val view = object : ErrorStateView {
            override fun showErrorState(error: InfrastructureError): Action {
                return showAction
            }

            override fun hideErrorState(): Action {
                return hideAction
            }

        }

        errorStateAssigner = AssignErrorState(view, uiScheduler)
    }

    @Test fun `should not show error state when flow emmits`() {
        Observable.just("A", "B", "C")
                .compose(errorStateAssigner)
                .subscribe()

        `verify hidden at start and no more displayed`()
    }

    @Test fun `should not show error state when flow is empty`() {
        Observable.empty<Any>()
                .compose(errorStateAssigner)
                .subscribe()

        `verify hidden at start and no more displayed`()
    }

    @Test fun `should not show error state when error othar than data access signaled`() {
        Observable.error<Any>(IllegalAccessError("Ops"))
                .compose(errorStateAssigner)
                .subscribe(SilentObserver)

        `verify hidden at start and no more displayed`()
    }

    @Test fun `should show error when remote system is down`() {
        Observable.error<Any>(InfrastructureError.RemoteSystemDown)
                .compose(errorStateAssigner)
                .subscribe(SilentObserver)

        `verify hidden at start and displayed after`()
    }

    @Test fun `should show error when invalid data returned`() {
        Observable.error<Any>(InfrastructureError.UndesiredResponse)
                .compose(errorStateAssigner)
                .subscribe(SilentObserver)

        `verify hidden at start and displayed after`()
    }

    private fun `verify hidden at start and no more displayed`() {
        verify(hideAction, atLeastOnce()).run()
        verify(showAction, never()).run()
    }

    private fun `verify hidden at start and displayed after`() {
        verify(hideAction, atLeastOnce()).run()
        verify(showAction, atLeastOnce()).run()
    }
}