package br.ufs.nubankchallenge.core.tests.presentation

import br.ufs.nubankchallenge.core.domain.chargeback.Chargeback
import br.ufs.nubankchallenge.core.domain.chargeback.CreditCardSecurity
import br.ufs.nubankchallenge.core.domain.chargeback.PreventiveCardBlocking
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackOptions
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackReclaim
import br.ufs.nubankchallenge.core.domain.chargeback.models.Fraud
import br.ufs.nubankchallenge.core.presentation.chargeback.ChargebackScreen
import br.ufs.nubankchallenge.core.presentation.chargeback.ChargebackScreenModel
import br.ufs.nubankchallenge.core.presentation.chargeback.LockpadState.*
import br.ufs.nubankchallenge.core.tests.util.Fixtures.chargebackOptions
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 *
 * Created by @ubiratanfsoares
 *
 */


@RunWith(RobolectricTestRunner::class)
class ChargebackScreenTests {

    lateinit var chargebacker: Chargeback
    lateinit var cardSecurer: CreditCardSecurity
    lateinit var fraudPreventer: PreventiveCardBlocking

    lateinit var screen: ChargebackScreen

    @Before fun `before each test`() {
        chargebacker = mock()
        cardSecurer = mock()
        fraudPreventer = PreventiveCardBlocking(cardSecurer)
        screen = ChargebackScreen(fraudPreventer, cardSecurer, chargebacker)
    }

    @Test fun `should retrieve possible actions for chargeback`() {
        val options = chargebackOptions(blockCard = false)
        val expected = ChargebackScreenModel(options, UnlockedByDefault)

        whenever(chargebacker.possibleActions())
                .thenReturn(Observable.just(options))

        screen.chargebackOptions()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue { it == expected }
    }

    @Test fun `should block creditcard at user decision`() {

        val options = chargebackOptions(blockCard = false)
        val expected = ChargebackScreenModel(options, LockedByUser)

        `backend will accept card blocking`()
        `prepare screen with chargeback options`(options)

        screen.blockCreditcard()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue { it == expected }
    }

    @Test fun `should unblock creditcard at user decision`() {

        val given = chargebackOptions(blockCard = true)
        val expected = ChargebackScreenModel(
                options = given,
                actualLockpadState = UnlockedByUser
        )

        `backend will accept card blocking`()
        `prepare screen with chargeback options`(given)
        `backend now accepts card unblocking`()

        screen.unblockCreditcard()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue { it == expected }
    }

    @Test fun `should integrate creditcard preventive blocking`() {

        val options = chargebackOptions(blockCard = true)

        `backend will accept card blocking`()
        `prepare screen with chargeback options`(options)

        screen.chargebackOptions()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue { it.lockpadState == LockedBySystem }
    }

    @Test fun `should dispatch new reclaim for chargeback`() {
        val options = chargebackOptions(blockCard = false)

        whenever(chargebacker.possibleActions()).thenReturn(Observable.just(options))
        whenever(chargebacker.sendReclaim(any())).thenReturn(operationSuccess())


        val reclaim = ChargebackReclaim(
                userHistory = "Não fui eu!",
                frauds = listOf(Fraud("merchant_recognized", false))
        )

        screen.sendChargebackReclaim(reclaim)
                .test()
                .assertNoErrors()
                .assertComplete()
    }

    private fun `backend now accepts card unblocking`() {
        whenever(cardSecurer.unblockSolicitation()).thenReturn(operationSuccess())
    }

    private fun `backend will accept card blocking`() {
        whenever(cardSecurer.blockSolicitation()).thenReturn(operationSuccess())
    }

    private fun `prepare screen with chargeback options`(options: ChargebackOptions) {
        whenever(chargebacker.possibleActions())
                .thenReturn(Observable.just(options))

        screen.chargebackOptions().test().assertComplete()
    }

    private fun operationSuccess() = Observable.just(Unit)
}