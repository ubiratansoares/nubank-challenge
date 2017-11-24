package br.ufs.nubankchallenge.core.presentation.chargeback

import android.arch.lifecycle.ViewModel
import br.ufs.nubankchallenge.core.domain.chargeback.CreditCardSecurity
import br.ufs.nubankchallenge.core.domain.chargeback.PreventiveCardBlocking
import br.ufs.nubankchallenge.core.domain.chargeback.RetrieveChargebackOptions
import br.ufs.nubankchallenge.core.domain.chargeback.SubmitNewChargeback
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackReclaim
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.internal.operators.observable.ObservableNever
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

    private var replayer: Observable<ChargebackScreenModel> = Observable.never()

    fun chargebackOptions(invalidate: Boolean = false): Observable<ChargebackScreenModel> {
        if (invalidate) reset()
        if (replayer == CLEAR_STATE) replayer = optionsReplayer()
        return replayer
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

    private fun optionsReplayer(): Observable<ChargebackScreenModel> {
        return retrieveChargeback
                .possibleActions()
                .compose(preventiveCardBlocking)
                .map { ChargebackScreenModel(it) }
                .doOnNext { actualState = it }
                .observeOn(uiScheduler)
                .replay(BUFFER_COUNT)
                .autoConnect(MAX_SUBSCRIBERS)
    }

    private fun reset() { replayer = Observable.never() }

    private companion object {
        val CLEAR_STATE: Observable<*> = ObservableNever.INSTANCE
        val MAX_SUBSCRIBERS = 1
        val BUFFER_COUNT = 1
    }
}