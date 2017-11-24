package br.ufs.nubankchallenge.core.presentation.chargeback

import android.arch.lifecycle.ViewModel
import br.ufs.nubankchallenge.core.domain.chargeback.CreditCardSecurity
import br.ufs.nubankchallenge.core.domain.chargeback.PreventiveCardBlocking
import br.ufs.nubankchallenge.core.domain.chargeback.RetrieveChargebackOptions
import br.ufs.nubankchallenge.core.domain.chargeback.SubmitNewChargeback
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackReclaim
import br.ufs.nubankchallenge.core.presentation.util.Replayer
import br.ufs.nubankchallenge.core.presentation.util.Replayer.Companion.CLEAR_STATE
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class ChargebackScreen(
        private val preventiveCardBlocking: PreventiveCardBlocking,
        private val cardSecurity: CreditCardSecurity,
        private val retrieveChargeback: RetrieveChargebackOptions,
        private val submitNewChargeback: SubmitNewChargeback,
        private val uiScheduler: Scheduler = Schedulers.trampoline()) : ViewModel() {

    lateinit var actualState: ChargebackScreenModel
    lateinit var chargebackReclaim: ChargebackReclaim

    private var replayableOperation: Observable<ChargebackScreenModel> = Observable.never()

    fun chargebackOptions(invalidate: Boolean = false): Observable<ChargebackScreenModel> {
        if (invalidate) reset()
        if (replayableOperation == CLEAR_STATE) replayableOperation = replayableState()
        return replayableOperation
    }

    fun unblockCreditcard() =
            cardSecurity
                    .unblockSolicitation()
                    .map { actualState.copy(cardBlockedByUser = false) }

    fun blockCreditcard() =
            cardSecurity
                    .blockSolicitation()
                    .map { actualState.copy(cardBlockedByUser = true) }

    fun requestChargeback() = submitNewChargeback.withReclaim(chargebackReclaim)

    private fun replayableState(): Observable<ChargebackScreenModel> {
        return retrieveChargeback
                .possibleActions()
                .compose(preventiveCardBlocking)
                .map { ChargebackScreenModel(it) }
                .doOnNext { actualState = it }
                .observeOn(uiScheduler)
                .compose(Replayer())
    }

    private fun reset() { replayableOperation = Observable.never() }

}