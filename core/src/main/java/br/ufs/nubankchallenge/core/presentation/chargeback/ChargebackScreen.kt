package br.ufs.nubankchallenge.core.presentation.chargeback

import android.arch.lifecycle.ViewModel
import br.ufs.nubankchallenge.core.domain.chargeback.Chargeback
import br.ufs.nubankchallenge.core.domain.chargeback.PreventiveCardBlocking
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackReclaim
import br.ufs.nubankchallenge.core.domain.chargeback.models.Fraud
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
        private val fraudPreventer: PreventiveCardBlocking,
        private val chargebacker: Chargeback,
        private val uiScheduler: Scheduler = Schedulers.trampoline()) : ViewModel() {

    lateinit var actualState: ChargebackScreenModel

    private var replayable: Observable<ChargebackScreenModel> = Observable.never()

    fun chargebackOptions(invalidate: Boolean = false): Observable<ChargebackScreenModel> {
        if (invalidate) reset()
        if (replayable == CLEAR_STATE) replayable = replayableState()
        return replayable
    }

    fun sendChargebackReclaim(comment: String, choosedReasons: List<ReasonRowModel>)
            : Observable<Unit> {

        val reclaim = ChargebackReclaim(
                userHistory = comment,
                frauds = choosedReasons.map { Fraud(it.id, it.choosedByUser) }
        )

        return chargebacker
                .sendReclaim(reclaim)
                .observeOn(uiScheduler)
    }

    private fun replayableState(): Observable<ChargebackScreenModel> {
        return chargebacker
                .possibleActions()
                .compose(fraudPreventer)
                .map { ChargebackScreenModel(it, CreditcardState.UnblockedByDefault) }
                .doOnNext { actualState = it }
                .observeOn(uiScheduler)
                .compose(Replayer())
    }

    private fun reset() {
        replayable = Observable.never()
    }

}