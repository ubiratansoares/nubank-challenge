package br.ufs.nubankchallenge.core.presentation.chargeback

import android.arch.lifecycle.ViewModel
import br.ufs.nubankchallenge.core.domain.chargeback.CreditCardSecurity
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class CreditcardBlockerScreen(
        private val cardSecurer: CreditCardSecurity,
        private val uiScheduler: Scheduler = Schedulers.trampoline()) : ViewModel() {

    fun unblockCreditcard(): Observable<CreditcardState> =
            cardSecurer
                    .unblockSolicitation()
                    .map { CreditcardState.UnblockedByUser as CreditcardState }
                    .observeOn(uiScheduler)

    fun blockCreditcard(): Observable<CreditcardState> =
            cardSecurer
                    .blockSolicitation()
                    .map { CreditcardState.BlockedByUser as CreditcardState }
                    .observeOn(uiScheduler)
}