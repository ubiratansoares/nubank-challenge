package br.ufs.hiring.nubankchallenge.factories

import br.ufs.nubankchallenge.core.domain.chargeback.PreventiveCardBlocking
import br.ufs.nubankchallenge.core.presentation.behaviors.BehaviorsPresenter
import br.ufs.nubankchallenge.core.presentation.chargeback.ChargebackScreen
import br.ufs.nubankchallenge.core.presentation.notice.NoticeScreen
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 *
 * Created by @ubiratanfsoares
 *
 */

object PresentationFactory {

    private val uiScheduler = AndroidSchedulers.mainThread()

    fun behaviorsPresenter(view: Any) = BehaviorsPresenter(view, uiScheduler)

    fun noticeScreen() = NoticeScreen(InfrastructureFactory.notice(), uiScheduler)

    fun chargebackScreen(): ChargebackScreen {
        val blocker = InfrastructureFactory.cardBlocker()
        val preventiveBlocking = PreventiveCardBlocking(blocker)
        val chargebacker = InfrastructureFactory.chargeback()
        return ChargebackScreen(preventiveBlocking, blocker, chargebacker, uiScheduler)
    }

}