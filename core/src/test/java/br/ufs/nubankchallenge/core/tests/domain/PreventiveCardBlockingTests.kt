package br.ufs.nubankchallenge.core.tests.domain

import br.ufs.nubankchallenge.core.domain.chargeback.CreditCardSecurity
import br.ufs.nubankchallenge.core.domain.chargeback.PreventiveCardBlocking
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackOptions
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.RemoteSystemDown
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue.*
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

/**
 *
 * Created by @ubiratanfsoares
 *
 */


class PreventiveCardBlockingTests {

    val blockNotForced = ChargebackOptions(
            "Não reconheço essa compra",
            rawCommentHint = "Descreva o que aconteceu",
            shouldBlockCreditcard = false,
            possibleReasons = emptyList()
    )

    val blockNeeded = blockNotForced.copy(shouldBlockCreditcard = true)

    lateinit var blockPreventively: PreventiveCardBlocking
    lateinit var creditcardSecurity: CreditCardSecurity

    @Before fun `before each test`() {
        creditcardSecurity = mock()
        blockPreventively = PreventiveCardBlocking(creditcardSecurity)
    }

    @Test fun `should not block when not asked to by server`() {

        Observable.just(blockNotForced)
                .compose(blockPreventively)
                .test()
                .assertNoErrors()
                .assertComplete()

        verifyZeroInteractions(creditcardSecurity)
    }

    @Test fun `should block preventively when asked by server`() {

        whenever(creditcardSecurity.blockSolicitation())
                .thenReturn(blockedWithSuccess())

        Observable.just(blockNeeded)
                .compose(blockPreventively)
                .test()
                .assertNoErrors()
                .assertComplete()

        `verify block soliticitation only and once`()
    }

    @Test fun `should ignore autoblocking, given internet unreachable at blocking operation`() {

        whenever(creditcardSecurity.blockSolicitation())
                .thenReturn(internetUnreachable())

        `should complete switching chargeback options to card not blocked`()
        `verify block soliticitation only and once`()
    }

    @Test fun `should ignore autoblocking, given infrastructure error at blocking operation`() {

        whenever(creditcardSecurity.blockSolicitation()).
                thenReturn(infrastructureError())

        `should complete switching chargeback options to card not blocked`()
        `verify block soliticitation only and once`()
    }

    @Test fun `should fail, given timeout error at blocking operation`() {

        whenever(creditcardSecurity.blockSolicitation()).
                thenReturn(timeout())

        `verify error fowarded and chargeback options ignored`(OperationTimeout)
    }

    @Test fun `should fail, given connection error at blocking operation`() {

        whenever(creditcardSecurity.blockSolicitation()).
                thenReturn(connectionBroken())

        `verify error fowarded and chargeback options ignored`(ConnectionSpike)
    }

    private fun `verify error fowarded and chargeback options ignored`(error: Throwable) {

        Observable.just(blockNeeded)
                .compose(blockPreventively)
                .test()
                .assertError { it == error }
                .assertNoValues()
    }

    private fun `should complete switching chargeback options to card not blocked`() {

        Observable.just(blockNeeded)
                .compose(blockPreventively)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue { it == blockNotForced }
    }

    private fun `verify block soliticitation only and once`() {

        verify(creditcardSecurity, times(1)).blockSolicitation()
        verify(creditcardSecurity, never()).unblockSolicitation()
        verifyNoMoreInteractions(creditcardSecurity)
    }

    fun blockedWithSuccess() = Observable.just(Unit)
    fun internetUnreachable() = Observable.error<Unit>(InternetUnreachable)
    fun timeout() = Observable.error<Unit>(OperationTimeout)
    fun connectionBroken() = Observable.error<Unit>(ConnectionSpike)
    fun infrastructureError() = Observable.error<Unit>(RemoteSystemDown)

}


