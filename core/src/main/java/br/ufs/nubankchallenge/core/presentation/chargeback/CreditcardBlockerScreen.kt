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

    fun unblockCreditcard(): Observable<LockpadState> =
            cardSecurer
                    .unblockSolicitation()
                    .map { LockpadState.UnlockedByUser as LockpadState }
                    .observeOn(uiScheduler)

    fun blockCreditcard(): Observable<LockpadState> =
            cardSecurer
                    .blockSolicitation()
                    .map { LockpadState.LockedByUser as LockpadState }
                    .observeOn(uiScheduler)
}