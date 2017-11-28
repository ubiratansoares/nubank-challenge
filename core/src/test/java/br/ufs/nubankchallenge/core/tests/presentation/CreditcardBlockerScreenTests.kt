package br.ufs.nubankchallenge.core.tests.presentation

import br.ufs.nubankchallenge.core.domain.chargeback.CreditCardSecurity
import br.ufs.nubankchallenge.core.presentation.chargeback.CreditcardBlockerScreen
import br.ufs.nubankchallenge.core.presentation.chargeback.CreditcardState
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

/**
 *
 * Created by @ubiratanfsoares
 *
 */
class CreditcardBlockerScreenTests {

    lateinit var cardSecurer: CreditCardSecurity
    lateinit var screen: CreditcardBlockerScreen

    @Before fun `before each test`() {
        cardSecurer = mock()
        screen = CreditcardBlockerScreen(cardSecurer)
    }

    @Test fun `should block creditcard at user decision`() {

        `backend will accept card blocking`()

        screen.blockCreditcard()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue { it == CreditcardState.LockedByUser }
    }

    @Test fun `should unblock creditcard at user decision`() {

        `backend will accept card blocking`()
        `backend now accepts card unblocking`()

        screen.unblockCreditcard()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue { it == CreditcardState.UnlockedByUser }
    }

    private fun `backend now accepts card unblocking`() {
        whenever(cardSecurer.unblockSolicitation()).thenReturn(operationSuccess())
    }

    private fun `backend will accept card blocking`() {
        whenever(cardSecurer.blockSolicitation()).thenReturn(operationSuccess())
    }

    private fun operationSuccess() = Observable.just(Unit)

}
